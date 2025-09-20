package com.ridehub.user.domain;

import static com.ridehub.user.domain.AppUserTestSamples.*;
import static com.ridehub.user.domain.ProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ridehub.user.web.rest.TestUtil;
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
    void profileTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Profile profileBack = getProfileRandomSampleGenerator();

        appUser.setProfile(profileBack);
        assertThat(appUser.getProfile()).isEqualTo(profileBack);

        appUser.profile(null);
        assertThat(appUser.getProfile()).isNull();
    }
}
