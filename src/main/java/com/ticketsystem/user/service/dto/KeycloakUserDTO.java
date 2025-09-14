package com.ticketsystem.user.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.ticketsystem.user.domain.KeycloakUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KeycloakUserDTO implements Serializable {

    @NotNull
    private UUID id;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private UUID realmId;

    @NotNull
    private UUID userId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getRealmId() {
        return realmId;
    }

    public void setRealmId(UUID realmId) {
        this.realmId = realmId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KeycloakUserDTO)) {
            return false;
        }

        KeycloakUserDTO keycloakUserDTO = (KeycloakUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, keycloakUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KeycloakUserDTO{" +
            "id='" + getId() + "'" +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", realmId='" + getRealmId() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
