package com.ticketsystem.user.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserCriteriaTest {

    @Test
    void newUserCriteriaHasAllFiltersNullTest() {
        var userCriteria = new UserCriteria();
        assertThat(userCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void userCriteriaFluentMethodsCreatesFiltersTest() {
        var userCriteria = new UserCriteria();

        setAllFilters(userCriteria);

        assertThat(userCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void userCriteriaCopyCreatesNullFilterTest() {
        var userCriteria = new UserCriteria();
        var copy = userCriteria.copy();

        assertThat(userCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(userCriteria)
        );
    }

    @Test
    void userCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userCriteria = new UserCriteria();
        setAllFilters(userCriteria);

        var copy = userCriteria.copy();

        assertThat(userCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(userCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userCriteria = new UserCriteria();

        assertThat(userCriteria).hasToString("UserCriteria{}");
    }

    private static void setAllFilters(UserCriteria userCriteria) {
        userCriteria.id();
        userCriteria.username();
        userCriteria.email();
        userCriteria.passwordHash();
        userCriteria.firstName();
        userCriteria.lastName();
        userCriteria.phoneNumber();
        userCriteria.dateOfBirth();
        userCriteria.createdAt();
        userCriteria.updatedAt();
        userCriteria.keycloakUserId();
        userCriteria.userAvatar();
        userCriteria.isActive();
        userCriteria.distinct();
    }

    private static Condition<UserCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUsername()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPasswordHash()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getDateOfBirth()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getKeycloakUserId()) &&
                condition.apply(criteria.getUserAvatar()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserCriteria> copyFiltersAre(UserCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUsername(), copy.getUsername()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPasswordHash(), copy.getPasswordHash()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getDateOfBirth(), copy.getDateOfBirth()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getKeycloakUserId(), copy.getKeycloakUserId()) &&
                condition.apply(criteria.getUserAvatar(), copy.getUserAvatar()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
