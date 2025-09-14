package com.ticketsystem.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A KeycloakUser.
 */
@Entity
@Table(name = "keycloak_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KeycloakUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36, nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "realm_id", length = 36, nullable = false)
    private UUID realmId;

    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "user_id", length = 36, nullable = false)
    private UUID userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public KeycloakUser id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public KeycloakUser username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public KeycloakUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getRealmId() {
        return this.realmId;
    }

    public KeycloakUser realmId(UUID realmId) {
        this.setRealmId(realmId);
        return this;
    }

    public void setRealmId(UUID realmId) {
        this.realmId = realmId;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public KeycloakUser userId(UUID userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KeycloakUser)) {
            return false;
        }
        return getId() != null && getId().equals(((KeycloakUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KeycloakUser{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", realmId='" + getRealmId() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
