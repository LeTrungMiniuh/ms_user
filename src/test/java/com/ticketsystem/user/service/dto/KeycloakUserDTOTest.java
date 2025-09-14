package com.ticketsystem.user.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ticketsystem.user.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class KeycloakUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeycloakUserDTO.class);
        KeycloakUserDTO keycloakUserDTO1 = new KeycloakUserDTO();
        keycloakUserDTO1.setId(UUID.randomUUID());
        KeycloakUserDTO keycloakUserDTO2 = new KeycloakUserDTO();
        assertThat(keycloakUserDTO1).isNotEqualTo(keycloakUserDTO2);
        keycloakUserDTO2.setId(keycloakUserDTO1.getId());
        assertThat(keycloakUserDTO1).isEqualTo(keycloakUserDTO2);
        keycloakUserDTO2.setId(UUID.randomUUID());
        assertThat(keycloakUserDTO1).isNotEqualTo(keycloakUserDTO2);
        keycloakUserDTO1.setId(null);
        assertThat(keycloakUserDTO1).isNotEqualTo(keycloakUserDTO2);
    }
}
