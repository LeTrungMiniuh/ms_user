package com.ticketsystem.user.service;

import com.ticketsystem.user.service.dto.KeycloakUserDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link com.ticketsystem.user.domain.KeycloakUser}.
 */
public interface KeycloakUserService {
    /**
     * Save a keycloakUser.
     *
     * @param keycloakUserDTO the entity to save.
     * @return the persisted entity.
     */
    KeycloakUserDTO save(KeycloakUserDTO keycloakUserDTO);

    /**
     * Updates a keycloakUser.
     *
     * @param keycloakUserDTO the entity to update.
     * @return the persisted entity.
     */
    KeycloakUserDTO update(KeycloakUserDTO keycloakUserDTO);

    /**
     * Partially updates a keycloakUser.
     *
     * @param keycloakUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<KeycloakUserDTO> partialUpdate(KeycloakUserDTO keycloakUserDTO);

    /**
     * Get all the keycloakUsers.
     *
     * @return the list of entities.
     */
    List<KeycloakUserDTO> findAll();

    /**
     * Get the "id" keycloakUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KeycloakUserDTO> findOne(UUID id);

    /**
     * Delete the "id" keycloakUser.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
