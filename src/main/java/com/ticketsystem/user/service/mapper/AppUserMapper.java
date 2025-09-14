package com.ticketsystem.user.service.mapper;

import com.ticketsystem.user.domain.AppUser;
import com.ticketsystem.user.domain.UserPreferences;
import com.ticketsystem.user.service.dto.AppUserDTO;
import com.ticketsystem.user.service.dto.UserPreferencesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "preferences", source = "preferences", qualifiedByName = "userPreferencesId")
    AppUserDTO toDto(AppUser s);

    @Named("userPreferencesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserPreferencesDTO toDtoUserPreferencesId(UserPreferences userPreferences);
}
