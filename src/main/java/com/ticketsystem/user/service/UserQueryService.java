package com.ticketsystem.user.service;

import com.ticketsystem.user.domain.*; // for static metamodels
import com.ticketsystem.user.domain.User;
import com.ticketsystem.user.repository.UserRepository;
import com.ticketsystem.user.service.criteria.UserCriteria;
import com.ticketsystem.user.service.dto.UserDTO;
import com.ticketsystem.user.service.mapper.UserMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link User} entities in the database.
 * The main input is a {@link UserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserQueryService extends QueryService<User> {

    private static final Logger LOG = LoggerFactory.getLogger(UserQueryService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserQueryService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Return a {@link List} of {@link UserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findByCriteria(UserCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<User> specification = createSpecification(criteria);
        return userMapper.toDto(userRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<User> specification = createSpecification(criteria);
        return userRepository.count(specification);
    }

    /**
     * Function to convert {@link UserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<User> createSpecification(UserCriteria criteria) {
        Specification<User> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), User_.id),
                buildStringSpecification(criteria.getUsername(), User_.username),
                buildStringSpecification(criteria.getEmail(), User_.email),
                buildStringSpecification(criteria.getPasswordHash(), User_.passwordHash),
                buildStringSpecification(criteria.getFirstName(), User_.firstName),
                buildStringSpecification(criteria.getLastName(), User_.lastName),
                buildStringSpecification(criteria.getPhoneNumber(), User_.phoneNumber),
                buildRangeSpecification(criteria.getDateOfBirth(), User_.dateOfBirth),
                buildRangeSpecification(criteria.getCreatedAt(), User_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), User_.updatedAt),
                buildSpecification(criteria.getKeycloakUserId(), User_.keycloakUserId),
                buildStringSpecification(criteria.getUserAvatar(), User_.userAvatar),
                buildSpecification(criteria.getIsActive(), User_.isActive)
            );
        }
        return specification;
    }
}
