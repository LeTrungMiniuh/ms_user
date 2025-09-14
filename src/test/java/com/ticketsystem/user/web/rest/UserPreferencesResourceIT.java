package com.ticketsystem.user.web.rest;

import static com.ticketsystem.user.domain.UserPreferencesAsserts.*;
import static com.ticketsystem.user.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketsystem.user.IntegrationTest;
import com.ticketsystem.user.domain.UserPreferences;
import com.ticketsystem.user.repository.UserPreferencesRepository;
import com.ticketsystem.user.service.dto.UserPreferencesDTO;
import com.ticketsystem.user.service.mapper.UserPreferencesMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserPreferencesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserPreferencesResourceIT {

    private static final String DEFAULT_PREFERRED_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_PREFERRED_LANGUAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EMAIL_NOTIFICATIONS = false;
    private static final Boolean UPDATED_EMAIL_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_SMS_NOTIFICATIONS = false;
    private static final Boolean UPDATED_SMS_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_PUSH_NOTIFICATIONS = false;
    private static final Boolean UPDATED_PUSH_NOTIFICATIONS = true;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserPreferencesRepository userPreferencesRepository;

    @Autowired
    private UserPreferencesMapper userPreferencesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserPreferencesMockMvc;

    private UserPreferences userPreferences;

    private UserPreferences insertedUserPreferences;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPreferences createEntity(EntityManager em) {
        UserPreferences userPreferences = new UserPreferences()
            .preferredLanguage(DEFAULT_PREFERRED_LANGUAGE)
            .emailNotifications(DEFAULT_EMAIL_NOTIFICATIONS)
            .smsNotifications(DEFAULT_SMS_NOTIFICATIONS)
            .pushNotifications(DEFAULT_PUSH_NOTIFICATIONS)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL);
        return userPreferences;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPreferences createUpdatedEntity(EntityManager em) {
        UserPreferences updatedUserPreferences = new UserPreferences()
            .preferredLanguage(UPDATED_PREFERRED_LANGUAGE)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .smsNotifications(UPDATED_SMS_NOTIFICATIONS)
            .pushNotifications(UPDATED_PUSH_NOTIFICATIONS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);
        return updatedUserPreferences;
    }

    @BeforeEach
    void initTest() {
        userPreferences = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserPreferences != null) {
            userPreferencesRepository.delete(insertedUserPreferences);
            insertedUserPreferences = null;
        }
    }

    @Test
    @Transactional
    void createUserPreferences() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserPreferences
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(userPreferences);
        var returnedUserPreferencesDTO = om.readValue(
            restUserPreferencesMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(userPreferencesDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserPreferencesDTO.class
        );

        // Validate the UserPreferences in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserPreferences = userPreferencesMapper.toEntity(returnedUserPreferencesDTO);
        assertUserPreferencesUpdatableFieldsEquals(returnedUserPreferences, getPersistedUserPreferences(returnedUserPreferences));

        insertedUserPreferences = returnedUserPreferences;
    }

    @Test
    @Transactional
    void createUserPreferencesWithExistingId() throws Exception {
        // Create the UserPreferences with an existing ID
        userPreferences.setId(1L);
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(userPreferences);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPreferencesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreferences in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserPreferences() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList
        restUserPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPreferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].preferredLanguage").value(hasItem(DEFAULT_PREFERRED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].emailNotifications").value(hasItem(DEFAULT_EMAIL_NOTIFICATIONS)))
            .andExpect(jsonPath("$.[*].smsNotifications").value(hasItem(DEFAULT_SMS_NOTIFICATIONS)))
            .andExpect(jsonPath("$.[*].pushNotifications").value(hasItem(DEFAULT_PUSH_NOTIFICATIONS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getUserPreferences() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get the userPreferences
        restUserPreferencesMockMvc
            .perform(get(ENTITY_API_URL_ID, userPreferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPreferences.getId().intValue()))
            .andExpect(jsonPath("$.preferredLanguage").value(DEFAULT_PREFERRED_LANGUAGE))
            .andExpect(jsonPath("$.emailNotifications").value(DEFAULT_EMAIL_NOTIFICATIONS))
            .andExpect(jsonPath("$.smsNotifications").value(DEFAULT_SMS_NOTIFICATIONS))
            .andExpect(jsonPath("$.pushNotifications").value(DEFAULT_PUSH_NOTIFICATIONS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getUserPreferencesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        Long id = userPreferences.getId();

        defaultUserPreferencesFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserPreferencesFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserPreferencesFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPreferredLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where preferredLanguage equals to
        defaultUserPreferencesFiltering(
            "preferredLanguage.equals=" + DEFAULT_PREFERRED_LANGUAGE,
            "preferredLanguage.equals=" + UPDATED_PREFERRED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPreferredLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where preferredLanguage in
        defaultUserPreferencesFiltering(
            "preferredLanguage.in=" + DEFAULT_PREFERRED_LANGUAGE + "," + UPDATED_PREFERRED_LANGUAGE,
            "preferredLanguage.in=" + UPDATED_PREFERRED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPreferredLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where preferredLanguage is not null
        defaultUserPreferencesFiltering("preferredLanguage.specified=true", "preferredLanguage.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPreferredLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where preferredLanguage contains
        defaultUserPreferencesFiltering(
            "preferredLanguage.contains=" + DEFAULT_PREFERRED_LANGUAGE,
            "preferredLanguage.contains=" + UPDATED_PREFERRED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPreferredLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where preferredLanguage does not contain
        defaultUserPreferencesFiltering(
            "preferredLanguage.doesNotContain=" + UPDATED_PREFERRED_LANGUAGE,
            "preferredLanguage.doesNotContain=" + DEFAULT_PREFERRED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllUserPreferencesByEmailNotificationsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where emailNotifications equals to
        defaultUserPreferencesFiltering(
            "emailNotifications.equals=" + DEFAULT_EMAIL_NOTIFICATIONS,
            "emailNotifications.equals=" + UPDATED_EMAIL_NOTIFICATIONS
        );
    }

    @Test
    @Transactional
    void getAllUserPreferencesByEmailNotificationsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where emailNotifications in
        defaultUserPreferencesFiltering(
            "emailNotifications.in=" + DEFAULT_EMAIL_NOTIFICATIONS + "," + UPDATED_EMAIL_NOTIFICATIONS,
            "emailNotifications.in=" + UPDATED_EMAIL_NOTIFICATIONS
        );
    }

    @Test
    @Transactional
    void getAllUserPreferencesByEmailNotificationsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where emailNotifications is not null
        defaultUserPreferencesFiltering("emailNotifications.specified=true", "emailNotifications.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPreferencesBySmsNotificationsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where smsNotifications equals to
        defaultUserPreferencesFiltering(
            "smsNotifications.equals=" + DEFAULT_SMS_NOTIFICATIONS,
            "smsNotifications.equals=" + UPDATED_SMS_NOTIFICATIONS
        );
    }

    @Test
    @Transactional
    void getAllUserPreferencesBySmsNotificationsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where smsNotifications in
        defaultUserPreferencesFiltering(
            "smsNotifications.in=" + DEFAULT_SMS_NOTIFICATIONS + "," + UPDATED_SMS_NOTIFICATIONS,
            "smsNotifications.in=" + UPDATED_SMS_NOTIFICATIONS
        );
    }

    @Test
    @Transactional
    void getAllUserPreferencesBySmsNotificationsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where smsNotifications is not null
        defaultUserPreferencesFiltering("smsNotifications.specified=true", "smsNotifications.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPushNotificationsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where pushNotifications equals to
        defaultUserPreferencesFiltering(
            "pushNotifications.equals=" + DEFAULT_PUSH_NOTIFICATIONS,
            "pushNotifications.equals=" + UPDATED_PUSH_NOTIFICATIONS
        );
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPushNotificationsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where pushNotifications in
        defaultUserPreferencesFiltering(
            "pushNotifications.in=" + DEFAULT_PUSH_NOTIFICATIONS + "," + UPDATED_PUSH_NOTIFICATIONS,
            "pushNotifications.in=" + UPDATED_PUSH_NOTIFICATIONS
        );
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPushNotificationsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where pushNotifications is not null
        defaultUserPreferencesFiltering("pushNotifications.specified=true", "pushNotifications.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where phone equals to
        defaultUserPreferencesFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where phone in
        defaultUserPreferencesFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where phone is not null
        defaultUserPreferencesFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where phone contains
        defaultUserPreferencesFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where phone does not contain
        defaultUserPreferencesFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where email equals to
        defaultUserPreferencesFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where email in
        defaultUserPreferencesFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where email is not null
        defaultUserPreferencesFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPreferencesByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where email contains
        defaultUserPreferencesFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList where email does not contain
        defaultUserPreferencesFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    private void defaultUserPreferencesFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserPreferencesShouldBeFound(shouldBeFound);
        defaultUserPreferencesShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserPreferencesShouldBeFound(String filter) throws Exception {
        restUserPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPreferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].preferredLanguage").value(hasItem(DEFAULT_PREFERRED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].emailNotifications").value(hasItem(DEFAULT_EMAIL_NOTIFICATIONS)))
            .andExpect(jsonPath("$.[*].smsNotifications").value(hasItem(DEFAULT_SMS_NOTIFICATIONS)))
            .andExpect(jsonPath("$.[*].pushNotifications").value(hasItem(DEFAULT_PUSH_NOTIFICATIONS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restUserPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserPreferencesShouldNotBeFound(String filter) throws Exception {
        restUserPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserPreferences() throws Exception {
        // Get the userPreferences
        restUserPreferencesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserPreferences() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPreferences
        UserPreferences updatedUserPreferences = userPreferencesRepository.findById(userPreferences.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserPreferences are not directly saved in db
        em.detach(updatedUserPreferences);
        updatedUserPreferences
            .preferredLanguage(UPDATED_PREFERRED_LANGUAGE)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .smsNotifications(UPDATED_SMS_NOTIFICATIONS)
            .pushNotifications(UPDATED_PUSH_NOTIFICATIONS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(updatedUserPreferences);

        restUserPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPreferencesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userPreferencesDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserPreferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserPreferencesToMatchAllProperties(updatedUserPreferences);
    }

    @Test
    @Transactional
    void putNonExistingUserPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreferences.setId(longCount.incrementAndGet());

        // Create the UserPreferences
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(userPreferences);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPreferencesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userPreferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreferences.setId(longCount.incrementAndGet());

        // Create the UserPreferences
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(userPreferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userPreferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreferences.setId(longCount.incrementAndGet());

        // Create the UserPreferences
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(userPreferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreferencesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPreferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserPreferencesWithPatch() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPreferences using partial update
        UserPreferences partialUpdatedUserPreferences = new UserPreferences();
        partialUpdatedUserPreferences.setId(userPreferences.getId());

        partialUpdatedUserPreferences
            .preferredLanguage(UPDATED_PREFERRED_LANGUAGE)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .smsNotifications(UPDATED_SMS_NOTIFICATIONS)
            .pushNotifications(UPDATED_PUSH_NOTIFICATIONS)
            .email(UPDATED_EMAIL);

        restUserPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPreferences.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserPreferences))
            )
            .andExpect(status().isOk());

        // Validate the UserPreferences in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserPreferencesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserPreferences, userPreferences),
            getPersistedUserPreferences(userPreferences)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserPreferencesWithPatch() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPreferences using partial update
        UserPreferences partialUpdatedUserPreferences = new UserPreferences();
        partialUpdatedUserPreferences.setId(userPreferences.getId());

        partialUpdatedUserPreferences
            .preferredLanguage(UPDATED_PREFERRED_LANGUAGE)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .smsNotifications(UPDATED_SMS_NOTIFICATIONS)
            .pushNotifications(UPDATED_PUSH_NOTIFICATIONS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);

        restUserPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPreferences.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserPreferences))
            )
            .andExpect(status().isOk());

        // Validate the UserPreferences in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserPreferencesUpdatableFieldsEquals(
            partialUpdatedUserPreferences,
            getPersistedUserPreferences(partialUpdatedUserPreferences)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreferences.setId(longCount.incrementAndGet());

        // Create the UserPreferences
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(userPreferences);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userPreferencesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userPreferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreferences.setId(longCount.incrementAndGet());

        // Create the UserPreferences
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(userPreferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userPreferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreferences.setId(longCount.incrementAndGet());

        // Create the UserPreferences
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(userPreferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userPreferencesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPreferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserPreferences() throws Exception {
        // Initialize the database
        insertedUserPreferences = userPreferencesRepository.saveAndFlush(userPreferences);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userPreferences
        restUserPreferencesMockMvc
            .perform(delete(ENTITY_API_URL_ID, userPreferences.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userPreferencesRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected UserPreferences getPersistedUserPreferences(UserPreferences userPreferences) {
        return userPreferencesRepository.findById(userPreferences.getId()).orElseThrow();
    }

    protected void assertPersistedUserPreferencesToMatchAllProperties(UserPreferences expectedUserPreferences) {
        assertUserPreferencesAllPropertiesEquals(expectedUserPreferences, getPersistedUserPreferences(expectedUserPreferences));
    }

    protected void assertPersistedUserPreferencesToMatchUpdatableProperties(UserPreferences expectedUserPreferences) {
        assertUserPreferencesAllUpdatablePropertiesEquals(expectedUserPreferences, getPersistedUserPreferences(expectedUserPreferences));
    }
}
