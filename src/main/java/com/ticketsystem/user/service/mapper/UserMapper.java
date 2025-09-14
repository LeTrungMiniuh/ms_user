package com.ticketsystem.user.service.mapper;

import com.ticketsystem.user.domain.User;
import com.ticketsystem.user.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link User} and its DTO {@link UserDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {}
