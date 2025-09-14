package com.ticketsystem.user.web.rest;

import static com.ticketsystem.user.domain.AppUserAsserts.*;
import static com.ticketsystem.user.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketsystem.user.IntegrationTest;
import com.ticketsystem.user.domain.AppUser;
import com.ticketsystem.user.domain.UserPreferences;
import com.ticketsystem.user.repository.AppUserRepository;
import com.ticketsystem.user.service.dto.AppUserDTO;
import com.ticketsystem.user.service.mapper.AppUserMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AppUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppUserResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "?O@\\)q.!*_s7";
    private static final String UPDATED_EMAIL = "dGr-M@\\1jbLQ..\\)Gr";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_VERIFIED = false;
    private static final Boolean UPDATED_IS_VERIFIED = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_LOGIN_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_LOGIN_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/app-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppUserMockMvc;

    private AppUser appUser;

    private AppUser insertedAppUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createEntity() {
        return new AppUser()
            .username(DEFAULT_USERNAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .idNumber(DEFAULT_ID_NUMBER)
            .nationality(DEFAULT_NATIONALITY)
            .profileImage(DEFAULT_PROFILE_IMAGE)
            .isVerified(DEFAULT_IS_VERIFIED)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .lastLoginAt(DEFAULT_LAST_LOGIN_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createUpdatedEntity() {
        return new AppUser()
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .nationality(UPDATED_NATIONALITY)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .isVerified(UPDATED_IS_VERIFIED)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .lastLoginAt(UPDATED_LAST_LOGIN_AT);
    }

    @BeforeEach
    void initTest() {
        appUser = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAppUser != null) {
            appUserRepository.delete(insertedAppUser);
            insertedAppUser = null;
        }
    }

    @Test
    @Transactional
    void createAppUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);
        var returnedAppUserDTO = om.readValue(
            restAppUserMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppUserDTO.class
        );

        // Validate the AppUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppUser = appUserMapper.toEntity(returnedAppUserDTO);
        assertAppUserUpdatableFieldsEquals(returnedAppUser, getPersistedAppUser(returnedAppUser));

        insertedAppUser = returnedAppUser;
    }

    @Test
    @Transactional
    void createAppUserWithExistingId() throws Exception {
        // Create the AppUser with an existing ID
        appUser.setId(1L);
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setUsername(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setEmail(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setIsActive(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setCreatedAt(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppUsers() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(DEFAULT_PROFILE_IMAGE)))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastLoginAt").value(hasItem(DEFAULT_LAST_LOGIN_AT.toString())));
    }

    @Test
    @Transactional
    void getAppUser() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL_ID, appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appUser.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY))
            .andExpect(jsonPath("$.profileImage").value(DEFAULT_PROFILE_IMAGE))
            .andExpect(jsonPath("$.isVerified").value(DEFAULT_IS_VERIFIED))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.lastLoginAt").value(DEFAULT_LAST_LOGIN_AT.toString()));
    }

    @Test
    @Transactional
    void getAppUsersByIdFiltering() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        Long id = appUser.getId();

        defaultAppUserFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAppUserFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAppUserFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppUsersByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where username equals to
        defaultAppUserFiltering("username.equals=" + DEFAULT_USERNAME, "username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where username in
        defaultAppUserFiltering("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME, "username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where username is not null
        defaultAppUserFiltering("username.specified=true", "username.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where username contains
        defaultAppUserFiltering("username.contains=" + DEFAULT_USERNAME, "username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where username does not contain
        defaultAppUserFiltering("username.doesNotContain=" + UPDATED_USERNAME, "username.doesNotContain=" + DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where email equals to
        defaultAppUserFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where email in
        defaultAppUserFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where email is not null
        defaultAppUserFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where email contains
        defaultAppUserFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where email does not contain
        defaultAppUserFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber equals to
        defaultAppUserFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber in
        defaultAppUserFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber is not null
        defaultAppUserFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber contains
        defaultAppUserFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber does not contain
        defaultAppUserFiltering("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER, "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstName equals to
        defaultAppUserFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstName in
        defaultAppUserFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstName is not null
        defaultAppUserFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstName contains
        defaultAppUserFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstName does not contain
        defaultAppUserFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastName equals to
        defaultAppUserFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastName in
        defaultAppUserFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastName is not null
        defaultAppUserFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastName contains
        defaultAppUserFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastName does not contain
        defaultAppUserFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateOfBirth equals to
        defaultAppUserFiltering("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH, "dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllAppUsersByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateOfBirth in
        defaultAppUserFiltering(
            "dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH,
            "dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateOfBirth is not null
        defaultAppUserFiltering("dateOfBirth.specified=true", "dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateOfBirth is greater than or equal to
        defaultAppUserFiltering(
            "dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH,
            "dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateOfBirth is less than or equal to
        defaultAppUserFiltering(
            "dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH,
            "dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateOfBirth is less than
        defaultAppUserFiltering("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH, "dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllAppUsersByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateOfBirth is greater than
        defaultAppUserFiltering("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH, "dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllAppUsersByIdNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where idNumber equals to
        defaultAppUserFiltering("idNumber.equals=" + DEFAULT_ID_NUMBER, "idNumber.equals=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByIdNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where idNumber in
        defaultAppUserFiltering("idNumber.in=" + DEFAULT_ID_NUMBER + "," + UPDATED_ID_NUMBER, "idNumber.in=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByIdNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where idNumber is not null
        defaultAppUserFiltering("idNumber.specified=true", "idNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByIdNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where idNumber contains
        defaultAppUserFiltering("idNumber.contains=" + DEFAULT_ID_NUMBER, "idNumber.contains=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByIdNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where idNumber does not contain
        defaultAppUserFiltering("idNumber.doesNotContain=" + UPDATED_ID_NUMBER, "idNumber.doesNotContain=" + DEFAULT_ID_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where nationality equals to
        defaultAppUserFiltering("nationality.equals=" + DEFAULT_NATIONALITY, "nationality.equals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllAppUsersByNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where nationality in
        defaultAppUserFiltering(
            "nationality.in=" + DEFAULT_NATIONALITY + "," + UPDATED_NATIONALITY,
            "nationality.in=" + UPDATED_NATIONALITY
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where nationality is not null
        defaultAppUserFiltering("nationality.specified=true", "nationality.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByNationalityContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where nationality contains
        defaultAppUserFiltering("nationality.contains=" + DEFAULT_NATIONALITY, "nationality.contains=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllAppUsersByNationalityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where nationality does not contain
        defaultAppUserFiltering("nationality.doesNotContain=" + UPDATED_NATIONALITY, "nationality.doesNotContain=" + DEFAULT_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllAppUsersByProfileImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where profileImage equals to
        defaultAppUserFiltering("profileImage.equals=" + DEFAULT_PROFILE_IMAGE, "profileImage.equals=" + UPDATED_PROFILE_IMAGE);
    }

    @Test
    @Transactional
    void getAllAppUsersByProfileImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where profileImage in
        defaultAppUserFiltering(
            "profileImage.in=" + DEFAULT_PROFILE_IMAGE + "," + UPDATED_PROFILE_IMAGE,
            "profileImage.in=" + UPDATED_PROFILE_IMAGE
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByProfileImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where profileImage is not null
        defaultAppUserFiltering("profileImage.specified=true", "profileImage.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByProfileImageContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where profileImage contains
        defaultAppUserFiltering("profileImage.contains=" + DEFAULT_PROFILE_IMAGE, "profileImage.contains=" + UPDATED_PROFILE_IMAGE);
    }

    @Test
    @Transactional
    void getAllAppUsersByProfileImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where profileImage does not contain
        defaultAppUserFiltering(
            "profileImage.doesNotContain=" + UPDATED_PROFILE_IMAGE,
            "profileImage.doesNotContain=" + DEFAULT_PROFILE_IMAGE
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByIsVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where isVerified equals to
        defaultAppUserFiltering("isVerified.equals=" + DEFAULT_IS_VERIFIED, "isVerified.equals=" + UPDATED_IS_VERIFIED);
    }

    @Test
    @Transactional
    void getAllAppUsersByIsVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where isVerified in
        defaultAppUserFiltering("isVerified.in=" + DEFAULT_IS_VERIFIED + "," + UPDATED_IS_VERIFIED, "isVerified.in=" + UPDATED_IS_VERIFIED);
    }

    @Test
    @Transactional
    void getAllAppUsersByIsVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where isVerified is not null
        defaultAppUserFiltering("isVerified.specified=true", "isVerified.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where isActive equals to
        defaultAppUserFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppUsersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where isActive in
        defaultAppUserFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppUsersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where isActive is not null
        defaultAppUserFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where createdAt equals to
        defaultAppUserFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAppUsersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where createdAt in
        defaultAppUserFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAppUsersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where createdAt is not null
        defaultAppUserFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByLastLoginAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastLoginAt equals to
        defaultAppUserFiltering("lastLoginAt.equals=" + DEFAULT_LAST_LOGIN_AT, "lastLoginAt.equals=" + UPDATED_LAST_LOGIN_AT);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastLoginAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastLoginAt in
        defaultAppUserFiltering(
            "lastLoginAt.in=" + DEFAULT_LAST_LOGIN_AT + "," + UPDATED_LAST_LOGIN_AT,
            "lastLoginAt.in=" + UPDATED_LAST_LOGIN_AT
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByLastLoginAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastLoginAt is not null
        defaultAppUserFiltering("lastLoginAt.specified=true", "lastLoginAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByPreferencesIsEqualToSomething() throws Exception {
        UserPreferences preferences;
        if (TestUtil.findAll(em, UserPreferences.class).isEmpty()) {
            appUserRepository.saveAndFlush(appUser);
            preferences = UserPreferencesResourceIT.createEntity(em);
        } else {
            preferences = TestUtil.findAll(em, UserPreferences.class).get(0);
        }
        em.persist(preferences);
        em.flush();
        appUser.setPreferences(preferences);
        appUserRepository.saveAndFlush(appUser);
        Long preferencesId = preferences.getId();
        // Get all the appUserList where preferences equals to preferencesId
        defaultAppUserShouldBeFound("preferencesId.equals=" + preferencesId);

        // Get all the appUserList where preferences equals to (preferencesId + 1)
        defaultAppUserShouldNotBeFound("preferencesId.equals=" + (preferencesId + 1));
    }

    private void defaultAppUserFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAppUserShouldBeFound(shouldBeFound);
        defaultAppUserShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppUserShouldBeFound(String filter) throws Exception {
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(DEFAULT_PROFILE_IMAGE)))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastLoginAt").value(hasItem(DEFAULT_LAST_LOGIN_AT.toString())));

        // Check, that the count call also returns 1
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppUserShouldNotBeFound(String filter) throws Exception {
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppUser() throws Exception {
        // Get the appUser
        restAppUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppUser() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUser
        AppUser updatedAppUser = appUserRepository.findById(appUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppUser are not directly saved in db
        em.detach(updatedAppUser);
        updatedAppUser
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .nationality(UPDATED_NATIONALITY)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .isVerified(UPDATED_IS_VERIFIED)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .lastLoginAt(UPDATED_LAST_LOGIN_AT);
        AppUserDTO appUserDTO = appUserMapper.toDto(updatedAppUser);

        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppUserToMatchAllProperties(updatedAppUser);
    }

    @Test
    @Transactional
    void putNonExistingAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .nationality(UPDATED_NATIONALITY)
            .isActive(UPDATED_IS_ACTIVE);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAppUser, appUser), getPersistedAppUser(appUser));
    }

    @Test
    @Transactional
    void fullUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .nationality(UPDATED_NATIONALITY)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .isVerified(UPDATED_IS_VERIFIED)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .lastLoginAt(UPDATED_LAST_LOGIN_AT);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUserUpdatableFieldsEquals(partialUpdatedAppUser, getPersistedAppUser(partialUpdatedAppUser));
    }

    @Test
    @Transactional
    void patchNonExistingAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appUserDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppUser() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appUser
        restAppUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, appUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appUserRepository.count();
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

    protected AppUser getPersistedAppUser(AppUser appUser) {
        return appUserRepository.findById(appUser.getId()).orElseThrow();
    }

    protected void assertPersistedAppUserToMatchAllProperties(AppUser expectedAppUser) {
        assertAppUserAllPropertiesEquals(expectedAppUser, getPersistedAppUser(expectedAppUser));
    }

    protected void assertPersistedAppUserToMatchUpdatableProperties(AppUser expectedAppUser) {
        assertAppUserAllUpdatablePropertiesEquals(expectedAppUser, getPersistedAppUser(expectedAppUser));
    }
}
