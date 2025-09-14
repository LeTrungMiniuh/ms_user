package com.ticketsystem.user.service.impl;

import com.ticketsystem.user.domain.UserPreferences;
import com.ticketsystem.user.repository.UserPreferencesRepository;
import com.ticketsystem.user.service.UserPreferencesService;
import com.ticketsystem.user.service.dto.UserPreferencesDTO;
import com.ticketsystem.user.service.mapper.UserPreferencesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketsystem.user.domain.UserPreferences}.
 */
@Service
@Transactional
public class UserPreferencesServiceImpl implements UserPreferencesService {

    private static final Logger LOG = LoggerFactory.getLogger(UserPreferencesServiceImpl.class);

    private final UserPreferencesRepository userPreferencesRepository;

    private final UserPreferencesMapper userPreferencesMapper;

    public UserPreferencesServiceImpl(UserPreferencesRepository userPreferencesRepository, UserPreferencesMapper userPreferencesMapper) {
        this.userPreferencesRepository = userPreferencesRepository;
        this.userPreferencesMapper = userPreferencesMapper;
    }

    @Override
    public UserPreferencesDTO save(UserPreferencesDTO userPreferencesDTO) {
        LOG.debug("Request to save UserPreferences : {}", userPreferencesDTO);
        UserPreferences userPreferences = userPreferencesMapper.toEntity(userPreferencesDTO);
        userPreferences = userPreferencesRepository.save(userPreferences);
        return userPreferencesMapper.toDto(userPreferences);
    }

    @Override
    public UserPreferencesDTO update(UserPreferencesDTO userPreferencesDTO) {
        LOG.debug("Request to update UserPreferences : {}", userPreferencesDTO);
        UserPreferences userPreferences = userPreferencesMapper.toEntity(userPreferencesDTO);
        userPreferences = userPreferencesRepository.save(userPreferences);
        return userPreferencesMapper.toDto(userPreferences);
    }

    @Override
    public Optional<UserPreferencesDTO> partialUpdate(UserPreferencesDTO userPreferencesDTO) {
        LOG.debug("Request to partially update UserPreferences : {}", userPreferencesDTO);

        return userPreferencesRepository
            .findById(userPreferencesDTO.getId())
            .map(existingUserPreferences -> {
                userPreferencesMapper.partialUpdate(existingUserPreferences, userPreferencesDTO);

                return existingUserPreferences;
            })
            .map(userPreferencesRepository::save)
            .map(userPreferencesMapper::toDto);
    }

    /**
     *  Get all the userPreferences where AppUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserPreferencesDTO> findAllWhereAppUserIsNull() {
        LOG.debug("Request to get all userPreferences where AppUser is null");
        return StreamSupport.stream(userPreferencesRepository.findAll().spliterator(), false)
            .filter(userPreferences -> userPreferences.getAppUser() == null)
            .map(userPreferencesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserPreferencesDTO> findOne(Long id) {
        LOG.debug("Request to get UserPreferences : {}", id);
        return userPreferencesRepository.findById(id).map(userPreferencesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserPreferences : {}", id);
        userPreferencesRepository.deleteById(id);
    }
}
