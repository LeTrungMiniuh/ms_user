package com.ticketsystem.user.repository;

import com.ticketsystem.user.domain.UserPreferences;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserPreferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long>, JpaSpecificationExecutor<UserPreferences> {}
