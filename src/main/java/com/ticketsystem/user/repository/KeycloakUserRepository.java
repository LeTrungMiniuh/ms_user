package com.ticketsystem.user.repository;

import com.ticketsystem.user.domain.KeycloakUser;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the KeycloakUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KeycloakUserRepository extends JpaRepository<KeycloakUser, UUID> {}
