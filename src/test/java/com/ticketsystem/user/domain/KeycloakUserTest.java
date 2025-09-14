package com.ticketsystem.user.domain;

import static com.ticketsystem.user.domain.KeycloakUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ticketsystem.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KeycloakUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeycloakUser.class);
        KeycloakUser keycloakUser1 = getKeycloakUserSample1();
        KeycloakUser keycloakUser2 = new KeycloakUser();
        assertThat(keycloakUser1).isNotEqualTo(keycloakUser2);

        keycloakUser2.setId(keycloakUser1.getId());
        assertThat(keycloakUser1).isEqualTo(keycloakUser2);

        keycloakUser2 = getKeycloakUserSample2();
        assertThat(keycloakUser1).isNotEqualTo(keycloakUser2);
    }
}
