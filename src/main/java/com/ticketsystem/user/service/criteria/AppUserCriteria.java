package com.ticketsystem.user.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ticketsystem.user.domain.AppUser} entity. This class is used
 * in {@link com.ticketsystem.user.web.rest.AppUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /app-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter username;

    private StringFilter email;

    private StringFilter phoneNumber;

    private StringFilter firstName;

    private StringFilter lastName;

    private LocalDateFilter dateOfBirth;

    private StringFilter idNumber;

    private StringFilter nationality;

    private StringFilter profileImage;

    private BooleanFilter isVerified;

    private BooleanFilter isActive;

    private InstantFilter createdAt;

    private InstantFilter lastLoginAt;

    private LongFilter preferencesId;

    private Boolean distinct;

    public AppUserCriteria() {}

    public AppUserCriteria(AppUserCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.username = other.optionalUsername().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.dateOfBirth = other.optionalDateOfBirth().map(LocalDateFilter::copy).orElse(null);
        this.idNumber = other.optionalIdNumber().map(StringFilter::copy).orElse(null);
        this.nationality = other.optionalNationality().map(StringFilter::copy).orElse(null);
        this.profileImage = other.optionalProfileImage().map(StringFilter::copy).orElse(null);
        this.isVerified = other.optionalIsVerified().map(BooleanFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.lastLoginAt = other.optionalLastLoginAt().map(InstantFilter::copy).orElse(null);
        this.preferencesId = other.optionalPreferencesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AppUserCriteria copy() {
        return new AppUserCriteria(this);
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

    public StringFilter getUsername() {
        return username;
    }

    public Optional<StringFilter> optionalUsername() {
        return Optional.ofNullable(username);
    }

    public StringFilter username() {
        if (username == null) {
            setUsername(new StringFilter());
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
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

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<StringFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            setPhoneNumber(new StringFilter());
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(firstName);
    }

    public StringFilter firstName() {
        if (firstName == null) {
            setFirstName(new StringFilter());
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(lastName);
    }

    public StringFilter lastName() {
        if (lastName == null) {
            setLastName(new StringFilter());
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public LocalDateFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public Optional<LocalDateFilter> optionalDateOfBirth() {
        return Optional.ofNullable(dateOfBirth);
    }

    public LocalDateFilter dateOfBirth() {
        if (dateOfBirth == null) {
            setDateOfBirth(new LocalDateFilter());
        }
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public StringFilter getIdNumber() {
        return idNumber;
    }

    public Optional<StringFilter> optionalIdNumber() {
        return Optional.ofNullable(idNumber);
    }

    public StringFilter idNumber() {
        if (idNumber == null) {
            setIdNumber(new StringFilter());
        }
        return idNumber;
    }

    public void setIdNumber(StringFilter idNumber) {
        this.idNumber = idNumber;
    }

    public StringFilter getNationality() {
        return nationality;
    }

    public Optional<StringFilter> optionalNationality() {
        return Optional.ofNullable(nationality);
    }

    public StringFilter nationality() {
        if (nationality == null) {
            setNationality(new StringFilter());
        }
        return nationality;
    }

    public void setNationality(StringFilter nationality) {
        this.nationality = nationality;
    }

    public StringFilter getProfileImage() {
        return profileImage;
    }

    public Optional<StringFilter> optionalProfileImage() {
        return Optional.ofNullable(profileImage);
    }

    public StringFilter profileImage() {
        if (profileImage == null) {
            setProfileImage(new StringFilter());
        }
        return profileImage;
    }

    public void setProfileImage(StringFilter profileImage) {
        this.profileImage = profileImage;
    }

    public BooleanFilter getIsVerified() {
        return isVerified;
    }

    public Optional<BooleanFilter> optionalIsVerified() {
        return Optional.ofNullable(isVerified);
    }

    public BooleanFilter isVerified() {
        if (isVerified == null) {
            setIsVerified(new BooleanFilter());
        }
        return isVerified;
    }

    public void setIsVerified(BooleanFilter isVerified) {
        this.isVerified = isVerified;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getLastLoginAt() {
        return lastLoginAt;
    }

    public Optional<InstantFilter> optionalLastLoginAt() {
        return Optional.ofNullable(lastLoginAt);
    }

    public InstantFilter lastLoginAt() {
        if (lastLoginAt == null) {
            setLastLoginAt(new InstantFilter());
        }
        return lastLoginAt;
    }

    public void setLastLoginAt(InstantFilter lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public LongFilter getPreferencesId() {
        return preferencesId;
    }

    public Optional<LongFilter> optionalPreferencesId() {
        return Optional.ofNullable(preferencesId);
    }

    public LongFilter preferencesId() {
        if (preferencesId == null) {
            setPreferencesId(new LongFilter());
        }
        return preferencesId;
    }

    public void setPreferencesId(LongFilter preferencesId) {
        this.preferencesId = preferencesId;
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
        final AppUserCriteria that = (AppUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(username, that.username) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(idNumber, that.idNumber) &&
            Objects.equals(nationality, that.nationality) &&
            Objects.equals(profileImage, that.profileImage) &&
            Objects.equals(isVerified, that.isVerified) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(lastLoginAt, that.lastLoginAt) &&
            Objects.equals(preferencesId, that.preferencesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            username,
            email,
            phoneNumber,
            firstName,
            lastName,
            dateOfBirth,
            idNumber,
            nationality,
            profileImage,
            isVerified,
            isActive,
            createdAt,
            lastLoginAt,
            preferencesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUsername().map(f -> "username=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalDateOfBirth().map(f -> "dateOfBirth=" + f + ", ").orElse("") +
            optionalIdNumber().map(f -> "idNumber=" + f + ", ").orElse("") +
            optionalNationality().map(f -> "nationality=" + f + ", ").orElse("") +
            optionalProfileImage().map(f -> "profileImage=" + f + ", ").orElse("") +
            optionalIsVerified().map(f -> "isVerified=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalLastLoginAt().map(f -> "lastLoginAt=" + f + ", ").orElse("") +
            optionalPreferencesId().map(f -> "preferencesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
