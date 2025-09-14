package com.ticketsystem.user.service.mapper;

import static com.ticketsystem.user.domain.UserPreferencesAsserts.*;
import static com.ticketsystem.user.domain.UserPreferencesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserPreferencesMapperTest {

    private UserPreferencesMapper userPreferencesMapper;

    @BeforeEach
    void setUp() {
        userPreferencesMapper = new UserPreferencesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserPreferencesSample1();
        var actual = userPreferencesMapper.toEntity(userPreferencesMapper.toDto(expected));
        assertUserPreferencesAllPropertiesEquals(expected, actual);
    }
}
