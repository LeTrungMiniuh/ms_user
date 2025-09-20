package com.ridehub.user.service;

import com.ridehub.user.domain.*; // for static metamodels
import com.ridehub.user.domain.Profile;
import com.ridehub.user.repository.ProfileRepository;
import com.ridehub.user.service.criteria.ProfileCriteria;
import com.ridehub.user.service.dto.ProfileDTO;
import com.ridehub.user.service.mapper.ProfileMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Profile} entities in the database.
 * The main input is a {@link ProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfileQueryService extends QueryService<Profile> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileQueryService.class);

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    public ProfileQueryService(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    /**
     * Return a {@link List} of {@link ProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfileDTO> findByCriteria(ProfileCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileMapper.toDto(profileRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfileCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileRepository.count(specification);
    }

    /**
     * Function to convert {@link ProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Profile> createSpecification(ProfileCriteria criteria) {
        Specification<Profile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Profile_.id),
                buildStringSpecification(criteria.getFullName(), Profile_.fullName),
                buildRangeSpecification(criteria.getBirthDate(), Profile_.birthDate),
                buildRangeSpecification(criteria.getCreatedAt(), Profile_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), Profile_.updatedAt),
                buildSpecification(criteria.getIsDeleted(), Profile_.isDeleted),
                buildRangeSpecification(criteria.getDeletedAt(), Profile_.deletedAt),
                buildSpecification(criteria.getDeletedBy(), Profile_.deletedBy),
                buildSpecification(criteria.getUserId(), root -> root.join(Profile_.user, JoinType.LEFT).get(AppUser_.id))
            );
        }
        return specification;
    }
}
