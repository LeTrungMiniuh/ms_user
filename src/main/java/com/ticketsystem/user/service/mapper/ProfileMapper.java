package com.ticketsystem.user.service.mapper;

import com.ticketsystem.user.domain.Profile;
import com.ticketsystem.user.service.dto.ProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profile} and its DTO {@link ProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfileMapper extends EntityMapper<ProfileDTO, Profile> {}
