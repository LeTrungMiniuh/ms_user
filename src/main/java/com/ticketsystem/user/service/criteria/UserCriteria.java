package com.ticketsystem.user.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ticketsystem.user.domain.User} entity. This class is used
 * in {@link com.ticketsystem.user.web.rest.UserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter username;

    private StringFilter email;

    private StringFilter passwordHash;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter phoneNumber;

    private LocalDateFilter dateOfBirth;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private UUIDFilter keycloakUserId;

    private StringFilter userAvatar;

    private BooleanFilter isActive;

    private Boolean distinct;

    public UserCriteria() {}

    public UserCriteria(UserCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.username = other.optionalUsername().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.passwordHash = other.optionalPasswordHash().map(StringFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.dateOfBirth = other.optionalDateOfBirth().map(LocalDateFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.keycloakUserId = other.optionalKeycloakUserId().map(UUIDFilter::copy).orElse(null);
        this.userAvatar = other.optionalUserAvatar().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserCriteria copy() {
        return new UserCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
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

    public StringFilter getPasswordHash() {
        return passwordHash;
    }

    public Optional<StringFilter> optionalPasswordHash() {
        return Optional.ofNullable(passwordHash);
    }

    public StringFilter passwordHash() {
        if (passwordHash == null) {
            setPasswordHash(new StringFilter());
        }
        return passwordHash;
    }

    public void setPasswordHash(StringFilter passwordHash) {
        this.passwordHash = passwordHash;
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

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<InstantFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUIDFilter getKeycloakUserId() {
        return keycloakUserId;
    }

    public Optional<UUIDFilter> optionalKeycloakUserId() {
        return Optional.ofNullable(keycloakUserId);
    }

    public UUIDFilter keycloakUserId() {
        if (keycloakUserId == null) {
            setKeycloakUserId(new UUIDFilter());
        }
        return keycloakUserId;
    }

    public void setKeycloakUserId(UUIDFilter keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }

    public StringFilter getUserAvatar() {
        return userAvatar;
    }

    public Optional<StringFilter> optionalUserAvatar() {
        return Optional.ofNullable(userAvatar);
    }

    public StringFilter userAvatar() {
        if (userAvatar == null) {
            setUserAvatar(new StringFilter());
        }
        return userAvatar;
    }

    public void setUserAvatar(StringFilter userAvatar) {
        this.userAvatar = userAvatar;
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
        final UserCriteria that = (UserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(username, that.username) &&
            Objects.equals(email, that.email) &&
            Objects.equals(passwordHash, that.passwordHash) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(keycloakUserId, that.keycloakUserId) &&
            Objects.equals(userAvatar, that.userAvatar) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            username,
            email,
            passwordHash,
            firstName,
            lastName,
            phoneNumber,
            dateOfBirth,
            createdAt,
            updatedAt,
            keycloakUserId,
            userAvatar,
            isActive,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUsername().map(f -> "username=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPasswordHash().map(f -> "passwordHash=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalDateOfBirth().map(f -> "dateOfBirth=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalKeycloakUserId().map(f -> "keycloakUserId=" + f + ", ").orElse("") +
            optionalUserAvatar().map(f -> "userAvatar=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
