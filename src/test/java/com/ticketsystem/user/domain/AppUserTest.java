package com.ticketsystem.user.domain;

import static com.ticketsystem.user.domain.AppUserTestSamples.*;
import static com.ticketsystem.user.domain.UserPreferencesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ticketsystem.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = getAppUserSample1();
        AppUser appUser2 = new AppUser();
        assertThat(appUser1).isNotEqualTo(appUser2);

        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);

        appUser2 = getAppUserSample2();
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    void preferencesTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        UserPreferences userPreferencesBack = getUserPreferencesRandomSampleGenerator();

        appUser.setPreferences(userPreferencesBack);
        assertThat(appUser.getPreferences()).isEqualTo(userPreferencesBack);

        appUser.preferences(null);
        assertThat(appUser.getPreferences()).isNull();
    }
}
