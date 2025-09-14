package com.ticketsystem.user.service.mapper;

import com.ticketsystem.user.domain.UserPreferences;
import com.ticketsystem.user.service.dto.UserPreferencesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserPreferences} and its DTO {@link UserPreferencesDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserPreferencesMapper extends EntityMapper<UserPreferencesDTO, UserPreferences> {}
