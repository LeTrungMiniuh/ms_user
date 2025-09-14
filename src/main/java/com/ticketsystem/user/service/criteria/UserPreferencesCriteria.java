package com.ticketsystem.user.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ticketsystem.user.domain.UserPreferences} entity. This class is used
 * in {@link com.ticketsystem.user.web.rest.UserPreferencesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-preferences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPreferencesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter preferredLanguage;

    private BooleanFilter emailNotifications;

    private BooleanFilter smsNotifications;

    private BooleanFilter pushNotifications;

    private StringFilter phone;

    private StringFilter email;

    private LongFilter appUserId;

    private Boolean distinct;

    public UserPreferencesCriteria() {}

    public UserPreferencesCriteria(UserPreferencesCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.preferredLanguage = other.optionalPreferredLanguage().map(StringFilter::copy).orElse(null);
        this.emailNotifications = other.optionalEmailNotifications().map(BooleanFilter::copy).orElse(null);
        this.smsNotifications = other.optionalSmsNotifications().map(BooleanFilter::copy).orElse(null);
        this.pushNotifications = other.optionalPushNotifications().map(BooleanFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.appUserId = other.optionalAppUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserPreferencesCriteria copy() {
        return new UserPreferencesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPreferredLanguage() {
        return preferredLanguage;
    }

    public Optional<StringFilter> optionalPreferredLanguage() {
        return Optional.ofNullable(preferredLanguage);
    }

    public StringFilter preferredLanguage() {
        if (preferredLanguage == null) {
            setPreferredLanguage(new StringFilter());
        }
        return preferredLanguage;
    }

    public void setPreferredLanguage(StringFilter preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public BooleanFilter getEmailNotifications() {
        return emailNotifications;
    }

    public Optional<BooleanFilter> optionalEmailNotifications() {
        return Optional.ofNullable(emailNotifications);
    }

    public BooleanFilter emailNotifications() {
        if (emailNotifications == null) {
            setEmailNotifications(new BooleanFilter());
        }
        return emailNotifications;
    }

    public void setEmailNotifications(BooleanFilter emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public BooleanFilter getSmsNotifications() {
        return smsNotifications;
    }

    public Optional<BooleanFilter> optionalSmsNotifications() {
        return Optional.ofNullable(smsNotifications);
    }

    public BooleanFilter smsNotifications() {
        if (smsNotifications == null) {
            setSmsNotifications(new BooleanFilter());
        }
        return smsNotifications;
    }

    public void setSmsNotifications(BooleanFilter smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public BooleanFilter getPushNotifications() {
        return pushNotifications;
    }

    public Optional<BooleanFilter> optionalPushNotifications() {
        return Optional.ofNullable(pushNotifications);
    }

    public BooleanFilter pushNotifications() {
        if (pushNotifications == null) {
            setPushNotifications(new BooleanFilter());
        }
        return pushNotifications;
    }

    public void setPushNotifications(BooleanFilter pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public Optional<StringFilter> optionalPhone() {
        return Optional.ofNullable(phone);
    }

    public StringFilter phone() {
        if (phone == null) {
            setPhone(new StringFilter());
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getAppUserId() {
        return appUserId;
    }

    public Optional<LongFilter> optionalAppUserId() {
        return Optional.ofNullable(appUserId);
    }

    public LongFilter appUserId() {
        if (appUserId == null) {
            setAppUserId(new LongFilter());
        }
        return appUserId;
    }

    public void setAppUserId(LongFilter appUserId) {
        this.appUserId = appUserId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserPreferencesCriteria that = (UserPreferencesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(preferredLanguage, that.preferredLanguage) &&
            Objects.equals(emailNotifications, that.emailNotifications) &&
            Objects.equals(smsNotifications, that.smsNotifications) &&
            Objects.equals(pushNotifications, that.pushNotifications) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            preferredLanguage,
            emailNotifications,
            smsNotifications,
            pushNotifications,
            phone,
            email,
            appUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPreferencesCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPreferredLanguage().map(f -> "preferredLanguage=" + f + ", ").orElse("") +
            optionalEmailNotifications().map(f -> "emailNotifications=" + f + ", ").orElse("") +
            optionalSmsNotifications().map(f -> "smsNotifications=" + f + ", ").orElse("") +
            optionalPushNotifications().map(f -> "pushNotifications=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalAppUserId().map(f -> "appUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
