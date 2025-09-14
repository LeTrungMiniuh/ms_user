package com.ticketsystem.user.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ticketsystem.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPreferencesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPreferencesDTO.class);
        UserPreferencesDTO userPreferencesDTO1 = new UserPreferencesDTO();
        userPreferencesDTO1.setId(1L);
        UserPreferencesDTO userPreferencesDTO2 = new UserPreferencesDTO();
        assertThat(userPreferencesDTO1).isNotEqualTo(userPreferencesDTO2);
        userPreferencesDTO2.setId(userPreferencesDTO1.getId());
        assertThat(userPreferencesDTO1).isEqualTo(userPreferencesDTO2);
        userPreferencesDTO2.setId(2L);
        assertThat(userPreferencesDTO1).isNotEqualTo(userPreferencesDTO2);
        userPreferencesDTO1.setId(null);
        assertThat(userPreferencesDTO1).isNotEqualTo(userPreferencesDTO2);
    }
}
