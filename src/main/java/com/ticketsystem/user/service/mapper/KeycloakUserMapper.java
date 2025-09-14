package com.ticketsystem.user.service.mapper;

import com.ticketsystem.user.domain.KeycloakUser;
import com.ticketsystem.user.service.dto.KeycloakUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link KeycloakUser} and its DTO {@link KeycloakUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface KeycloakUserMapper extends EntityMapper<KeycloakUserDTO, KeycloakUser> {}
