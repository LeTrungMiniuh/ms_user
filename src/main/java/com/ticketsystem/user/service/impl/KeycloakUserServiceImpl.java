package com.ticketsystem.user.service.impl;

import com.ticketsystem.user.domain.KeycloakUser;
import com.ticketsystem.user.repository.KeycloakUserRepository;
import com.ticketsystem.user.service.KeycloakUserService;
import com.ticketsystem.user.service.dto.KeycloakUserDTO;
import com.ticketsystem.user.service.mapper.KeycloakUserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketsystem.user.domain.KeycloakUser}.
 */
@Service
@Transactional
public class KeycloakUserServiceImpl implements KeycloakUserService {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakUserServiceImpl.class);

    private final KeycloakUserRepository keycloakUserRepository;

    private final KeycloakUserMapper keycloakUserMapper;

    public KeycloakUserServiceImpl(KeycloakUserRepository keycloakUserRepository, KeycloakUserMapper keycloakUserMapper) {
        this.keycloakUserRepository = keycloakUserRepository;
        this.keycloakUserMapper = keycloakUserMapper;
    }

    @Override
    public KeycloakUserDTO save(KeycloakUserDTO keycloakUserDTO) {
        LOG.debug("Request to save KeycloakUser : {}", keycloakUserDTO);
        KeycloakUser keycloakUser = keycloakUserMapper.toEntity(keycloakUserDTO);
        keycloakUser = keycloakUserRepository.save(keycloakUser);
        return keycloakUserMapper.toDto(keycloakUser);
    }

    @Override
    public KeycloakUserDTO update(KeycloakUserDTO keycloakUserDTO) {
        LOG.debug("Request to update KeycloakUser : {}", keycloakUserDTO);
        KeycloakUser keycloakUser = keycloakUserMapper.toEntity(keycloakUserDTO);
        keycloakUser = keycloakUserRepository.save(keycloakUser);
        return keycloakUserMapper.toDto(keycloakUser);
    }

    @Override
    public Optional<KeycloakUserDTO> partialUpdate(KeycloakUserDTO keycloakUserDTO) {
        LOG.debug("Request to partially update KeycloakUser : {}", keycloakUserDTO);

        return keycloakUserRepository
            .findById(keycloakUserDTO.getId())
            .map(existingKeycloakUser -> {
                keycloakUserMapper.partialUpdate(existingKeycloakUser, keycloakUserDTO);

                return existingKeycloakUser;
            })
            .map(keycloakUserRepository::save)
            .map(keycloakUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KeycloakUserDTO> findAll() {
        LOG.debug("Request to get all KeycloakUsers");
        return keycloakUserRepository.findAll().stream().map(keycloakUserMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<KeycloakUserDTO> findOne(UUID id) {
        LOG.debug("Request to get KeycloakUser : {}", id);
        return keycloakUserRepository.findById(id).map(keycloakUserMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete KeycloakUser : {}", id);
        keycloakUserRepository.deleteById(id);
    }
}
