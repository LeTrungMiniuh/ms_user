package com.ticketsystem.user.service;

import com.ticketsystem.user.service.dto.UserDTO;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link com.ticketsystem.user.domain.User}.
 */
public interface UserService {
    /**
     * Save a user.
     *
     * @param userDTO the entity to save.
     * @return the persisted entity.
     */
    UserDTO save(UserDTO userDTO);

    /**
     * Updates a user.
     *
     * @param userDTO the entity to update.
     * @return the persisted entity.
     */
    UserDTO update(UserDTO userDTO);

    /**
     * Partially updates a user.
     *
     * @param userDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserDTO> partialUpdate(UserDTO userDTO);

    /**
     * Get the "id" user.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserDTO> findOne(UUID id);

    /**
     * Delete the "id" user.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
