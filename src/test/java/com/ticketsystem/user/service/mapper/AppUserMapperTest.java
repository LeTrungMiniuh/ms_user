package com.ticketsystem.user.service.mapper;

import static com.ticketsystem.user.domain.AppUserAsserts.*;
import static com.ticketsystem.user.domain.AppUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppUserMapperTest {

    private AppUserMapper appUserMapper;

    @BeforeEach
    void setUp() {
        appUserMapper = new AppUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppUserSample1();
        var actual = appUserMapper.toEntity(appUserMapper.toDto(expected));
        assertAppUserAllPropertiesEquals(expected, actual);
    }
}
