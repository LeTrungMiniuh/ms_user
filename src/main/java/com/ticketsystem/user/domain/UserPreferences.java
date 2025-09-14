package com.ticketsystem.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserPreferences.
 */
@Entity
@Table(name = "user_preferences")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPreferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "preferred_language")
    private String preferredLanguage;

    @Column(name = "email_notifications")
    private Boolean emailNotifications;

    @Column(name = "sms_notifications")
    private Boolean smsNotifications;

    @Column(name = "push_notifications")
    private Boolean pushNotifications;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @JsonIgnoreProperties(value = { "preferences" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "preferences")
    private AppUser appUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserPreferences id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreferredLanguage() {
        return this.preferredLanguage;
    }

    public UserPreferences preferredLanguage(String preferredLanguage) {
        this.setPreferredLanguage(preferredLanguage);
        return this;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public Boolean getEmailNotifications() {
        return this.emailNotifications;
    }

    public UserPreferences emailNotifications(Boolean emailNotifications) {
        this.setEmailNotifications(emailNotifications);
        return this;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getSmsNotifications() {
        return this.smsNotifications;
    }

    public UserPreferences smsNotifications(Boolean smsNotifications) {
        this.setSmsNotifications(smsNotifications);
        return this;
    }

    public void setSmsNotifications(Boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public Boolean getPushNotifications() {
        return this.pushNotifications;
    }

    public UserPreferences pushNotifications(Boolean pushNotifications) {
        this.setPushNotifications(pushNotifications);
        return this;
    }

    public void setPushNotifications(Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserPreferences phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public UserPreferences email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        if (this.appUser != null) {
            this.appUser.setPreferences(null);
        }
        if (appUser != null) {
            appUser.setPreferences(this);
        }
        this.appUser = appUser;
    }

    public UserPreferences appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPreferences)) {
            return false;
        }
        return getId() != null && getId().equals(((UserPreferences) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPreferences{" +
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
