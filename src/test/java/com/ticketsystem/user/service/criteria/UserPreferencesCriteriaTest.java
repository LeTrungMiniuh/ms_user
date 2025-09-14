package com.ticketsystem.user.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserPreferencesCriteriaTest {

    @Test
    void newUserPreferencesCriteriaHasAllFiltersNullTest() {
        var userPreferencesCriteria = new UserPreferencesCriteria();
        assertThat(userPreferencesCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void userPreferencesCriteriaFluentMethodsCreatesFiltersTest() {
        var userPreferencesCriteria = new UserPreferencesCriteria();

        setAllFilters(userPreferencesCriteria);

        assertThat(userPreferencesCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void userPreferencesCriteriaCopyCreatesNullFilterTest() {
        var userPreferencesCriteria = new UserPreferencesCriteria();
        var copy = userPreferencesCriteria.copy();

        assertThat(userPreferencesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(userPreferencesCriteria)
        );
    }

    @Test
    void userPreferencesCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userPreferencesCriteria = new UserPreferencesCriteria();
        setAllFilters(userPreferencesCriteria);

        var copy = userPreferencesCriteria.copy();

        assertThat(userPreferencesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(userPreferencesCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userPreferencesCriteria = new UserPreferencesCriteria();

        assertThat(userPreferencesCriteria).hasToString("UserPreferencesCriteria{}");
    }

    private static void setAllFilters(UserPreferencesCriteria userPreferencesCriteria) {
        userPreferencesCriteria.id();
        userPreferencesCriteria.preferredLanguage();
        userPreferencesCriteria.emailNotifications();
        userPreferencesCriteria.smsNotifications();
        userPreferencesCriteria.pushNotifications();
        userPreferencesCriteria.phone();
        userPreferencesCriteria.email();
        userPreferencesCriteria.appUserId();
        userPreferencesCriteria.distinct();
    }

    private static Condition<UserPreferencesCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPreferredLanguage()) &&
                condition.apply(criteria.getEmailNotifications()) &&
                condition.apply(criteria.getSmsNotifications()) &&
                condition.apply(criteria.getPushNotifications()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getAppUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserPreferencesCriteria> copyFiltersAre(
        UserPreferencesCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPreferredLanguage(), copy.getPreferredLanguage()) &&
                condition.apply(criteria.getEmailNotifications(), copy.getEmailNotifications()) &&
                condition.apply(criteria.getSmsNotifications(), copy.getSmsNotifications()) &&
                condition.apply(criteria.getPushNotifications(), copy.getPushNotifications()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getAppUserId(), copy.getAppUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
