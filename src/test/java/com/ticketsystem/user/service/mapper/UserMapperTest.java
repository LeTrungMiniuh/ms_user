package com.ticketsystem.user.service.mapper;

import static com.ticketsystem.user.domain.UserAsserts.*;
import static com.ticketsystem.user.domain.UserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserSample1();
        var actual = userMapper.toEntity(userMapper.toDto(expected));
        assertUserAllPropertiesEquals(expected, actual);
    }
}
