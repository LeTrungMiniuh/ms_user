package com.ticketsystem.user.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AppUserCriteriaTest {

    @Test
    void newAppUserCriteriaHasAllFiltersNullTest() {
        var appUserCriteria = new AppUserCriteria();
        assertThat(appUserCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void appUserCriteriaFluentMethodsCreatesFiltersTest() {
        var appUserCriteria = new AppUserCriteria();

        setAllFilters(appUserCriteria);

        assertThat(appUserCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void appUserCriteriaCopyCreatesNullFilterTest() {
        var appUserCriteria = new AppUserCriteria();
        var copy = appUserCriteria.copy();

        assertThat(appUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(appUserCriteria)
        );
    }

    @Test
    void appUserCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var appUserCriteria = new AppUserCriteria();
        setAllFilters(appUserCriteria);

        var copy = appUserCriteria.copy();

        assertThat(appUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(appUserCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var appUserCriteria = new AppUserCriteria();

        assertThat(appUserCriteria).hasToString("AppUserCriteria{}");
    }

    private static void setAllFilters(AppUserCriteria appUserCriteria) {
        appUserCriteria.id();
        appUserCriteria.username();
        appUserCriteria.email();
        appUserCriteria.phoneNumber();
        appUserCriteria.firstName();
        appUserCriteria.lastName();
        appUserCriteria.dateOfBirth();
        appUserCriteria.idNumber();
        appUserCriteria.nationality();
        appUserCriteria.profileImage();
        appUserCriteria.isVerified();
        appUserCriteria.isActive();
        appUserCriteria.createdAt();
        appUserCriteria.lastLoginAt();
        appUserCriteria.preferencesId();
        appUserCriteria.distinct();
    }

    private static Condition<AppUserCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUsername()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getDateOfBirth()) &&
                condition.apply(criteria.getIdNumber()) &&
                condition.apply(criteria.getNationality()) &&
                condition.apply(criteria.getProfileImage()) &&
                condition.apply(criteria.getIsVerified()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getLastLoginAt()) &&
                condition.apply(criteria.getPreferencesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AppUserCriteria> copyFiltersAre(AppUserCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUsername(), copy.getUsername()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getDateOfBirth(), copy.getDateOfBirth()) &&
                condition.apply(criteria.getIdNumber(), copy.getIdNumber()) &&
                condition.apply(criteria.getNationality(), copy.getNationality()) &&
                condition.apply(criteria.getProfileImage(), copy.getProfileImage()) &&
                condition.apply(criteria.getIsVerified(), copy.getIsVerified()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getLastLoginAt(), copy.getLastLoginAt()) &&
                condition.apply(criteria.getPreferencesId(), copy.getPreferencesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
