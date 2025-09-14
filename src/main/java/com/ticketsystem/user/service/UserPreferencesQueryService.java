package com.ticketsystem.user.service;

import com.ticketsystem.user.domain.*; // for static metamodels
import com.ticketsystem.user.domain.UserPreferences;
import com.ticketsystem.user.repository.UserPreferencesRepository;
import com.ticketsystem.user.service.criteria.UserPreferencesCriteria;
import com.ticketsystem.user.service.dto.UserPreferencesDTO;
import com.ticketsystem.user.service.mapper.UserPreferencesMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UserPreferences} entities in the database.
 * The main input is a {@link UserPreferencesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserPreferencesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserPreferencesQueryService extends QueryService<UserPreferences> {

    private static final Logger LOG = LoggerFactory.getLogger(UserPreferencesQueryService.class);

    private final UserPreferencesRepository userPreferencesRepository;

    private final UserPreferencesMapper userPreferencesMapper;

    public UserPreferencesQueryService(UserPreferencesRepository userPreferencesRepository, UserPreferencesMapper userPreferencesMapper) {
        this.userPreferencesRepository = userPreferencesRepository;
        this.userPreferencesMapper = userPreferencesMapper;
    }

    /**
     * Return a {@link List} of {@link UserPreferencesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserPreferencesDTO> findByCriteria(UserPreferencesCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<UserPreferences> specification = createSpecification(criteria);
        return userPreferencesMapper.toDto(userPreferencesRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserPreferencesCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserPreferences> specification = createSpecification(criteria);
        return userPreferencesRepository.count(specification);
    }

    /**
     * Function to convert {@link UserPreferencesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserPreferences> createSpecification(UserPreferencesCriteria criteria) {
        Specification<UserPreferences> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), UserPreferences_.id),
                buildStringSpecification(criteria.getPreferredLanguage(), UserPreferences_.preferredLanguage),
                buildSpecification(criteria.getEmailNotifications(), UserPreferences_.emailNotifications),
                buildSpecification(criteria.getSmsNotifications(), UserPreferences_.smsNotifications),
                buildSpecification(criteria.getPushNotifications(), UserPreferences_.pushNotifications),
                buildStringSpecification(criteria.getPhone(), UserPreferences_.phone),
                buildStringSpecification(criteria.getEmail(), UserPreferences_.email),
                buildSpecification(criteria.getAppUserId(), root -> root.join(UserPreferences_.appUser, JoinType.LEFT).get(AppUser_.id))
            );
        }
        return specification;
    }
}
