package com.ticketsystem.user.web.rest;

import static com.ticketsystem.user.domain.KeycloakUserAsserts.*;
import static com.ticketsystem.user.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketsystem.user.IntegrationTest;
import com.ticketsystem.user.domain.KeycloakUser;
import com.ticketsystem.user.repository.KeycloakUserRepository;
import com.ticketsystem.user.service.dto.KeycloakUserDTO;
import com.ticketsystem.user.service.mapper.KeycloakUserMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
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
 * Integration tests for the {@link KeycloakUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KeycloakUserResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final UUID DEFAULT_REALM_ID = UUID.randomUUID();
    private static final UUID UPDATED_REALM_ID = UUID.randomUUID();

    private static final UUID DEFAULT_USER_ID = UUID.randomUUID();
    private static final UUID UPDATED_USER_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/keycloak-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KeycloakUserRepository keycloakUserRepository;

    @Autowired
    private KeycloakUserMapper keycloakUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKeycloakUserMockMvc;

    private KeycloakUser keycloakUser;

    private KeycloakUser insertedKeycloakUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeycloakUser createEntity() {
        return new KeycloakUser().username(DEFAULT_USERNAME).email(DEFAULT_EMAIL).realmId(DEFAULT_REALM_ID).userId(DEFAULT_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KeycloakUser createUpdatedEntity() {
        return new KeycloakUser().username(UPDATED_USERNAME).email(UPDATED_EMAIL).realmId(UPDATED_REALM_ID).userId(UPDATED_USER_ID);
    }

    @BeforeEach
    void initTest() {
        keycloakUser = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedKeycloakUser != null) {
            keycloakUserRepository.delete(insertedKeycloakUser);
            insertedKeycloakUser = null;
        }
    }

    @Test
    @Transactional
    void createKeycloakUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the KeycloakUser
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);
        var returnedKeycloakUserDTO = om.readValue(
            restKeycloakUserMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keycloakUserDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            KeycloakUserDTO.class
        );

        // Validate the KeycloakUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedKeycloakUser = keycloakUserMapper.toEntity(returnedKeycloakUserDTO);
        assertKeycloakUserUpdatableFieldsEquals(returnedKeycloakUser, getPersistedKeycloakUser(returnedKeycloakUser));

        insertedKeycloakUser = returnedKeycloakUser;
    }

    @Test
    @Transactional
    void createKeycloakUserWithExistingId() throws Exception {
        // Create the KeycloakUser with an existing ID
        insertedKeycloakUser = keycloakUserRepository.saveAndFlush(keycloakUser);
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeycloakUserMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeycloakUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        keycloakUser.setUsername(null);

        // Create the KeycloakUser, which fails.
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);

        restKeycloakUserMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        keycloakUser.setEmail(null);

        // Create the KeycloakUser, which fails.
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);

        restKeycloakUserMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRealmIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        keycloakUser.setRealmId(null);

        // Create the KeycloakUser, which fails.
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);

        restKeycloakUserMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        keycloakUser.setUserId(null);

        // Create the KeycloakUser, which fails.
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);

        restKeycloakUserMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKeycloakUsers() throws Exception {
        // Initialize the database
        insertedKeycloakUser = keycloakUserRepository.saveAndFlush(keycloakUser);

        // Get all the keycloakUserList
        restKeycloakUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keycloakUser.getId().toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].realmId").value(hasItem(DEFAULT_REALM_ID.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())));
    }

    @Test
    @Transactional
    void getKeycloakUser() throws Exception {
        // Initialize the database
        insertedKeycloakUser = keycloakUserRepository.saveAndFlush(keycloakUser);

        // Get the keycloakUser
        restKeycloakUserMockMvc
            .perform(get(ENTITY_API_URL_ID, keycloakUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(keycloakUser.getId().toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.realmId").value(DEFAULT_REALM_ID.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()));
    }

    @Test
    @Transactional
    void getNonExistingKeycloakUser() throws Exception {
        // Get the keycloakUser
        restKeycloakUserMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKeycloakUser() throws Exception {
        // Initialize the database
        insertedKeycloakUser = keycloakUserRepository.saveAndFlush(keycloakUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the keycloakUser
        KeycloakUser updatedKeycloakUser = keycloakUserRepository.findById(keycloakUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedKeycloakUser are not directly saved in db
        em.detach(updatedKeycloakUser);
        updatedKeycloakUser.username(UPDATED_USERNAME).email(UPDATED_EMAIL).realmId(UPDATED_REALM_ID).userId(UPDATED_USER_ID);
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(updatedKeycloakUser);

        restKeycloakUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, keycloakUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the KeycloakUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKeycloakUserToMatchAllProperties(updatedKeycloakUser);
    }

    @Test
    @Transactional
    void putNonExistingKeycloakUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keycloakUser.setId(UUID.randomUUID());

        // Create the KeycloakUser
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeycloakUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, keycloakUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeycloakUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKeycloakUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keycloakUser.setId(UUID.randomUUID());

        // Create the KeycloakUser
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeycloakUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeycloakUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKeycloakUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keycloakUser.setId(UUID.randomUUID());

        // Create the KeycloakUser
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeycloakUserMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KeycloakUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKeycloakUserWithPatch() throws Exception {
        // Initialize the database
        insertedKeycloakUser = keycloakUserRepository.saveAndFlush(keycloakUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the keycloakUser using partial update
        KeycloakUser partialUpdatedKeycloakUser = new KeycloakUser();
        partialUpdatedKeycloakUser.setId(keycloakUser.getId());

        restKeycloakUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKeycloakUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKeycloakUser))
            )
            .andExpect(status().isOk());

        // Validate the KeycloakUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKeycloakUserUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedKeycloakUser, keycloakUser),
            getPersistedKeycloakUser(keycloakUser)
        );
    }

    @Test
    @Transactional
    void fullUpdateKeycloakUserWithPatch() throws Exception {
        // Initialize the database
        insertedKeycloakUser = keycloakUserRepository.saveAndFlush(keycloakUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the keycloakUser using partial update
        KeycloakUser partialUpdatedKeycloakUser = new KeycloakUser();
        partialUpdatedKeycloakUser.setId(keycloakUser.getId());

        partialUpdatedKeycloakUser.username(UPDATED_USERNAME).email(UPDATED_EMAIL).realmId(UPDATED_REALM_ID).userId(UPDATED_USER_ID);

        restKeycloakUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKeycloakUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKeycloakUser))
            )
            .andExpect(status().isOk());

        // Validate the KeycloakUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKeycloakUserUpdatableFieldsEquals(partialUpdatedKeycloakUser, getPersistedKeycloakUser(partialUpdatedKeycloakUser));
    }

    @Test
    @Transactional
    void patchNonExistingKeycloakUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keycloakUser.setId(UUID.randomUUID());

        // Create the KeycloakUser
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKeycloakUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, keycloakUserDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeycloakUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKeycloakUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keycloakUser.setId(UUID.randomUUID());

        // Create the KeycloakUser
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeycloakUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KeycloakUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKeycloakUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        keycloakUser.setId(UUID.randomUUID());

        // Create the KeycloakUser
        KeycloakUserDTO keycloakUserDTO = keycloakUserMapper.toDto(keycloakUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKeycloakUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(keycloakUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KeycloakUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKeycloakUser() throws Exception {
        // Initialize the database
        insertedKeycloakUser = keycloakUserRepository.saveAndFlush(keycloakUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the keycloakUser
        restKeycloakUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, keycloakUser.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return keycloakUserRepository.count();
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

    protected KeycloakUser getPersistedKeycloakUser(KeycloakUser keycloakUser) {
        return keycloakUserRepository.findById(keycloakUser.getId()).orElseThrow();
    }

    protected void assertPersistedKeycloakUserToMatchAllProperties(KeycloakUser expectedKeycloakUser) {
        assertKeycloakUserAllPropertiesEquals(expectedKeycloakUser, getPersistedKeycloakUser(expectedKeycloakUser));
    }

    protected void assertPersistedKeycloakUserToMatchUpdatableProperties(KeycloakUser expectedKeycloakUser) {
        assertKeycloakUserAllUpdatablePropertiesEquals(expectedKeycloakUser, getPersistedKeycloakUser(expectedKeycloakUser));
    }
}
