package com.ridehub.user.service.mapper;

import com.ridehub.user.domain.AppUser;
import com.ridehub.user.domain.Profile;
import com.ridehub.user.service.dto.AppUserDTO;
import com.ridehub.user.service.dto.ProfileDTO;
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
