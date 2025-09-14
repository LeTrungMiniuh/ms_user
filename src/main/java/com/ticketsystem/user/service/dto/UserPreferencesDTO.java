package com.ticketsystem.user.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketsystem.user.domain.UserPreferences} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPreferencesDTO implements Serializable {

    private Long id;

    private String preferredLanguage;

    private Boolean emailNotifications;

    private Boolean smsNotifications;

    private Boolean pushNotifications;

    private String phone;

    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getSmsNotifications() {
        return smsNotifications;
    }

    public void setSmsNotifications(Boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public Boolean getPushNotifications() {
        return pushNotifications;
    }

    public void setPushNotifications(Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPreferencesDTO)) {
            return false;
        }

        UserPreferencesDTO userPreferencesDTO = (UserPreferencesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userPreferencesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPreferencesDTO{" +
            "id=" + getId() +
            ", preferredLanguage='" + getPreferredLanguage() + "'" +
            ", emailNotifications='" + getEmailNotifications() + "'" +
            ", smsNotifications='" + getSmsNotifications() + "'" +
            ", pushNotifications='" + getPushNotifications() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
