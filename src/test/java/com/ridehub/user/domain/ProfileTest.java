package com.ridehub.user.domain;

import static com.ridehub.user.domain.AppUserTestSamples.*;
import static com.ridehub.user.domain.ProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ridehub.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profile.class);
        Profile profile1 = getProfileSample1();
        Profile profile2 = new Profile();
        assertThat(profile1).isNotEqualTo(profile2);

        profile2.setId(profile1.getId());
        assertThat(profile1).isEqualTo(profile2);

        profile2 = getProfileSample2();
        assertThat(profile1).isNotEqualTo(profile2);
    }

    @Test
    void userTest() {
        Profile profile = getProfileRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        profile.setUser(appUserBack);
        assertThat(profile.getUser()).isEqualTo(appUserBack);
        assertThat(appUserBack.getProfile()).isEqualTo(profile);

        profile.user(null);
        assertThat(profile.getUser()).isNull();
        assertThat(appUserBack.getProfile()).isNull();
    }
}
