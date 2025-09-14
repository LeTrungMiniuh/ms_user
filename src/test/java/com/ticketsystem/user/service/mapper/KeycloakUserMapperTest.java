package com.ticketsystem.user.service.mapper;

import static com.ticketsystem.user.domain.KeycloakUserAsserts.*;
import static com.ticketsystem.user.domain.KeycloakUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KeycloakUserMapperTest {

    private KeycloakUserMapper keycloakUserMapper;

    @BeforeEach
    void setUp() {
        keycloakUserMapper = new KeycloakUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getKeycloakUserSample1();
        var actual = keycloakUserMapper.toEntity(keycloakUserMapper.toDto(expected));
        assertKeycloakUserAllPropertiesEquals(expected, actual);
    }
}
