package com.ticketsystem.user.service.mapper;

import com.ticketsystem.user.domain.AppUser;
import com.ticketsystem.user.domain.Profile;
import com.ticketsystem.user.service.dto.AppUserDTO;
import com.ticketsystem.user.service.dto.ProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "profile", source = "profile", qualifiedByName = "profileId")
    AppUserDTO toDto(AppUser s);

    @Named("profileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfileDTO toDtoProfileId(Profile profile);
}
