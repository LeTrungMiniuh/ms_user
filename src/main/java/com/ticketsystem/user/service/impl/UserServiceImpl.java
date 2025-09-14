package com.ticketsystem.user.service.impl;

import com.ticketsystem.user.domain.User;
import com.ticketsystem.user.repository.UserRepository;
import com.ticketsystem.user.service.UserService;
import com.ticketsystem.user.service.dto.UserDTO;
import com.ticketsystem.user.service.mapper.UserMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketsystem.user.domain.User}.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        LOG.debug("Request to save User : {}", userDTO);
        User user = userMapper.toEntity(userDTO);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        LOG.debug("Request to update User : {}", userDTO);
        User user = userMapper.toEntity(userDTO);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public Optional<UserDTO> partialUpdate(UserDTO userDTO) {
        LOG.debug("Request to partially update User : {}", userDTO);

        return userRepository
            .findById(userDTO.getId())
            .map(existingUser -> {
                userMapper.partialUpdate(existingUser, userDTO);

                return existingUser;
            })
            .map(userRepository::save)
            .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findOne(UUID id) {
        LOG.debug("Request to get User : {}", id);
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete User : {}", id);
        userRepository.deleteById(id);
    }
}
