package com.ticketsystem.user.domain;

import static com.ticketsystem.user.domain.AppUserTestSamples.*;
import static com.ticketsystem.user.domain.UserPreferencesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ticketsystem.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPreferencesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPreferences.class);
        UserPreferences userPreferences1 = getUserPreferencesSample1();
        UserPreferences userPreferences2 = new UserPreferences();
        assertThat(userPreferences1).isNotEqualTo(userPreferences2);

        userPreferences2.setId(userPreferences1.getId());
        assertThat(userPreferences1).isEqualTo(userPreferences2);

        userPreferences2 = getUserPreferencesSample2();
        assertThat(userPreferences1).isNotEqualTo(userPreferences2);
    }

    @Test
    void appUserTest() {
        UserPreferences userPreferences = getUserPreferencesRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        userPreferences.setAppUser(appUserBack);
        assertThat(userPreferences.getAppUser()).isEqualTo(appUserBack);
        assertThat(appUserBack.getPreferences()).isEqualTo(userPreferences);

        userPreferences.appUser(null);
        assertThat(userPreferences.getAppUser()).isNull();
        assertThat(appUserBack.getPreferences()).isNull();
    }
}
