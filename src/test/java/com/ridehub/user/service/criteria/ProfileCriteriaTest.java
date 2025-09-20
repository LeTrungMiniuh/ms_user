package com.ridehub.user.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProfileCriteriaTest {

    @Test
    void newProfileCriteriaHasAllFiltersNullTest() {
        var profileCriteria = new ProfileCriteria();
        assertThat(profileCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void profileCriteriaFluentMethodsCreatesFiltersTest() {
        var profileCriteria = new ProfileCriteria();

        setAllFilters(profileCriteria);

        assertThat(profileCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void profileCriteriaCopyCreatesNullFilterTest() {
        var profileCriteria = new ProfileCriteria();
        var copy = profileCriteria.copy();

        assertThat(profileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(profileCriteria)
        );
    }

    @Test
    void profileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var profileCriteria = new ProfileCriteria();
        setAllFilters(profileCriteria);

        var copy = profileCriteria.copy();

        assertThat(profileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(profileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var profileCriteria = new ProfileCriteria();

        assertThat(profileCriteria).hasToString("ProfileCriteria{}");
    }

    private static void setAllFilters(ProfileCriteria profileCriteria) {
        profileCriteria.id();
        profileCriteria.fullName();
        profileCriteria.birthDate();
        profileCriteria.createdAt();
        profileCriteria.updatedAt();
        profileCriteria.isDeleted();
        profileCriteria.deletedAt();
        profileCriteria.deletedBy();
        profileCriteria.userId();
        profileCriteria.distinct();
    }

    private static Condition<ProfileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getDeletedAt()) &&
                condition.apply(criteria.getDeletedBy()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProfileCriteria> copyFiltersAre(ProfileCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getDeletedAt(), copy.getDeletedAt()) &&
                condition.apply(criteria.getDeletedBy(), copy.getDeletedBy()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
