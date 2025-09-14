package com.ticketsystem.user.web.rest;

import static com.ticketsystem.user.domain.UserAsserts.*;
import static com.ticketsystem.user.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketsystem.user.IntegrationTest;
import com.ticketsystem.user.domain.User;
import com.ticketsystem.user.repository.UserRepository;
import com.ticketsystem.user.service.dto.UserDTO;
import com.ticketsystem.user.service.mapper.UserMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link UserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final UUID DEFAULT_KEYCLOAK_USER_ID = UUID.randomUUID();
    private static final UUID UPDATED_KEYCLOAK_USER_ID = UUID.randomUUID();

    private static final String DEFAULT_USER_AVATAR = "AAAAAAAAAA";
    private static final String UPDATED_USER_AVATAR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMockMvc;

    private User user;

    private User insertedUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createEntity() {
        return new User()
            .username(DEFAULT_USERNAME)
            .email(DEFAULT_EMAIL)
            .passwordHash(DEFAULT_PASSWORD_HASH)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .keycloakUserId(DEFAULT_KEYCLOAK_USER_ID)
            .userAvatar(DEFAULT_USER_AVATAR)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createUpdatedEntity() {
        return new User()
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .userAvatar(UPDATED_USER_AVATAR)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        user = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUser != null) {
            userRepository.delete(insertedUser);
            insertedUser = null;
        }
    }

    @Test
    @Transactional
    void createUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the User
        UserDTO userDTO = userMapper.toDto(user);
        var returnedUserDTO = om.readValue(
            restUserMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserDTO.class
        );

        // Validate the User in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUser = userMapper.toEntity(returnedUserDTO);
        assertUserUpdatableFieldsEquals(returnedUser, getPersistedUser(returnedUser));

        insertedUser = returnedUser;
    }

    @Test
    @Transactional
    void createUserWithExistingId() throws Exception {
        // Create the User with an existing ID
        insertedUser = userRepository.saveAndFlush(user);
        UserDTO userDTO = userMapper.toDto(user);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setUsername(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setEmail(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordHashIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setPasswordHash(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setFirstName(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setLastName(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setPhoneNumber(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setCreatedAt(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setUpdatedAt(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKeycloakUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setKeycloakUserId(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        user.setIsActive(null);

        // Create the User, which fails.
        UserDTO userDTO = userMapper.toDto(user);

        restUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList
        restUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(user.getId().toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].keycloakUserId").value(hasItem(DEFAULT_KEYCLOAK_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].userAvatar").value(hasItem(DEFAULT_USER_AVATAR)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getUser() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get the user
        restUserMockMvc
            .perform(get(ENTITY_API_URL_ID, user.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(user.getId().toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.passwordHash").value(DEFAULT_PASSWORD_HASH))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.keycloakUserId").value(DEFAULT_KEYCLOAK_USER_ID.toString()))
            .andExpect(jsonPath("$.userAvatar").value(DEFAULT_USER_AVATAR))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getUsersByIdFiltering() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        UUID id = user.getId();

        defaultUserFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllUsersByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where username equals to
        defaultUserFiltering("username.equals=" + DEFAULT_USERNAME, "username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUsersByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where username in
        defaultUserFiltering("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME, "username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUsersByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where username is not null
        defaultUserFiltering("username.specified=true", "username.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where username contains
        defaultUserFiltering("username.contains=" + DEFAULT_USERNAME, "username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllUsersByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where username does not contain
        defaultUserFiltering("username.doesNotContain=" + UPDATED_USERNAME, "username.doesNotContain=" + DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where email equals to
        defaultUserFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where email in
        defaultUserFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where email is not null
        defaultUserFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where email contains
        defaultUserFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where email does not contain
        defaultUserFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordHashIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where passwordHash equals to
        defaultUserFiltering("passwordHash.equals=" + DEFAULT_PASSWORD_HASH, "passwordHash.equals=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordHashIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where passwordHash in
        defaultUserFiltering(
            "passwordHash.in=" + DEFAULT_PASSWORD_HASH + "," + UPDATED_PASSWORD_HASH,
            "passwordHash.in=" + UPDATED_PASSWORD_HASH
        );
    }

    @Test
    @Transactional
    void getAllUsersByPasswordHashIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where passwordHash is not null
        defaultUserFiltering("passwordHash.specified=true", "passwordHash.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByPasswordHashContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where passwordHash contains
        defaultUserFiltering("passwordHash.contains=" + DEFAULT_PASSWORD_HASH, "passwordHash.contains=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordHashNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where passwordHash does not contain
        defaultUserFiltering(
            "passwordHash.doesNotContain=" + UPDATED_PASSWORD_HASH,
            "passwordHash.doesNotContain=" + DEFAULT_PASSWORD_HASH
        );
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where firstName equals to
        defaultUserFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where firstName in
        defaultUserFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where firstName is not null
        defaultUserFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where firstName contains
        defaultUserFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where firstName does not contain
        defaultUserFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where lastName equals to
        defaultUserFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where lastName in
        defaultUserFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where lastName is not null
        defaultUserFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where lastName contains
        defaultUserFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where lastName does not contain
        defaultUserFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where phoneNumber equals to
        defaultUserFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUsersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where phoneNumber in
        defaultUserFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllUsersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where phoneNumber is not null
        defaultUserFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where phoneNumber contains
        defaultUserFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUsersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where phoneNumber does not contain
        defaultUserFiltering("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER, "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUsersByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where dateOfBirth equals to
        defaultUserFiltering("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH, "dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllUsersByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where dateOfBirth in
        defaultUserFiltering(
            "dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH,
            "dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllUsersByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where dateOfBirth is not null
        defaultUserFiltering("dateOfBirth.specified=true", "dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where dateOfBirth is greater than or equal to
        defaultUserFiltering(
            "dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH,
            "dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllUsersByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where dateOfBirth is less than or equal to
        defaultUserFiltering(
            "dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH,
            "dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllUsersByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where dateOfBirth is less than
        defaultUserFiltering("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH, "dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllUsersByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where dateOfBirth is greater than
        defaultUserFiltering("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH, "dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where createdAt equals to
        defaultUserFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where createdAt in
        defaultUserFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where createdAt is not null
        defaultUserFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where updatedAt equals to
        defaultUserFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where updatedAt in
        defaultUserFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where updatedAt is not null
        defaultUserFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByKeycloakUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where keycloakUserId equals to
        defaultUserFiltering("keycloakUserId.equals=" + DEFAULT_KEYCLOAK_USER_ID, "keycloakUserId.equals=" + UPDATED_KEYCLOAK_USER_ID);
    }

    @Test
    @Transactional
    void getAllUsersByKeycloakUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where keycloakUserId in
        defaultUserFiltering(
            "keycloakUserId.in=" + DEFAULT_KEYCLOAK_USER_ID + "," + UPDATED_KEYCLOAK_USER_ID,
            "keycloakUserId.in=" + UPDATED_KEYCLOAK_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllUsersByKeycloakUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where keycloakUserId is not null
        defaultUserFiltering("keycloakUserId.specified=true", "keycloakUserId.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUserAvatarIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where userAvatar equals to
        defaultUserFiltering("userAvatar.equals=" + DEFAULT_USER_AVATAR, "userAvatar.equals=" + UPDATED_USER_AVATAR);
    }

    @Test
    @Transactional
    void getAllUsersByUserAvatarIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where userAvatar in
        defaultUserFiltering("userAvatar.in=" + DEFAULT_USER_AVATAR + "," + UPDATED_USER_AVATAR, "userAvatar.in=" + UPDATED_USER_AVATAR);
    }

    @Test
    @Transactional
    void getAllUsersByUserAvatarIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where userAvatar is not null
        defaultUserFiltering("userAvatar.specified=true", "userAvatar.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUserAvatarContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where userAvatar contains
        defaultUserFiltering("userAvatar.contains=" + DEFAULT_USER_AVATAR, "userAvatar.contains=" + UPDATED_USER_AVATAR);
    }

    @Test
    @Transactional
    void getAllUsersByUserAvatarNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where userAvatar does not contain
        defaultUserFiltering("userAvatar.doesNotContain=" + UPDATED_USER_AVATAR, "userAvatar.doesNotContain=" + DEFAULT_USER_AVATAR);
    }

    @Test
    @Transactional
    void getAllUsersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where isActive equals to
        defaultUserFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUsersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where isActive in
        defaultUserFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUsersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        // Get all the userList where isActive is not null
        defaultUserFiltering("isActive.specified=true", "isActive.specified=false");
    }

    private void defaultUserFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserShouldBeFound(shouldBeFound);
        defaultUserShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserShouldBeFound(String filter) throws Exception {
        restUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(user.getId().toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].keycloakUserId").value(hasItem(DEFAULT_KEYCLOAK_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].userAvatar").value(hasItem(DEFAULT_USER_AVATAR)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));

        // Check, that the count call also returns 1
        restUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserShouldNotBeFound(String filter) throws Exception {
        restUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUser() throws Exception {
        // Get the user
        restUserMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUser() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUser are not directly saved in db
        em.detach(updatedUser);
        updatedUser
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .userAvatar(UPDATED_USER_AVATAR)
            .isActive(UPDATED_IS_ACTIVE);
        UserDTO userDTO = userMapper.toDto(updatedUser);

        restUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDTO))
            )
            .andExpect(status().isOk());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserToMatchAllProperties(updatedUser);
    }

    @Test
    @Transactional
    void putNonExistingUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(UUID.randomUUID());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(UUID.randomUUID());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(UUID.randomUUID());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserWithPatch() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the user using partial update
        User partialUpdatedUser = new User();
        partialUpdatedUser.setId(user.getId());

        partialUpdatedUser
            .username(UPDATED_USERNAME)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .firstName(UPDATED_FIRST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .isActive(UPDATED_IS_ACTIVE);

        restUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUser))
            )
            .andExpect(status().isOk());

        // Validate the User in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUser, user), getPersistedUser(user));
    }

    @Test
    @Transactional
    void fullUpdateUserWithPatch() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the user using partial update
        User partialUpdatedUser = new User();
        partialUpdatedUser.setId(user.getId());

        partialUpdatedUser
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .userAvatar(UPDATED_USER_AVATAR)
            .isActive(UPDATED_IS_ACTIVE);

        restUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUser))
            )
            .andExpect(status().isOk());

        // Validate the User in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserUpdatableFieldsEquals(partialUpdatedUser, getPersistedUser(partialUpdatedUser));
    }

    @Test
    @Transactional
    void patchNonExistingUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(UUID.randomUUID());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(UUID.randomUUID());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(UUID.randomUUID());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUser() throws Exception {
        // Initialize the database
        insertedUser = userRepository.saveAndFlush(user);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the user
        restUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, user.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userRepository.count();
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

    protected User getPersistedUser(User user) {
        return userRepository.findById(user.getId()).orElseThrow();
    }

    protected void assertPersistedUserToMatchAllProperties(User expectedUser) {
        assertUserAllPropertiesEquals(expectedUser, getPersistedUser(expectedUser));
    }

    protected void assertPersistedUserToMatchUpdatableProperties(User expectedUser) {
        assertUserAllUpdatablePropertiesEquals(expectedUser, getPersistedUser(expectedUser));
    }
}
