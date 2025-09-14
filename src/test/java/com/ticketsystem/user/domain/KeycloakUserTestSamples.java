package com.ticketsystem.user.domain;

import java.util.UUID;

public class KeycloakUserTestSamples {

    public static KeycloakUser getKeycloakUserSample1() {
        return new KeycloakUser()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .username("username1")
            .email("email1")
            .realmId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .userId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static KeycloakUser getKeycloakUserSample2() {
        return new KeycloakUser()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .username("username2")
            .email("email2")
            .realmId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .userId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static KeycloakUser getKeycloakUserRandomSampleGenerator() {
        return new KeycloakUser()
            .id(UUID.randomUUID())
            .username(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .realmId(UUID.randomUUID())
            .userId(UUID.randomUUID());
    }
}
