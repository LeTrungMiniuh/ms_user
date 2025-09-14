package com.ticketsystem.user.domain;

import java.util.UUID;

public class UserTestSamples {

    public static User getUserSample1() {
        return new User()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .username("username1")
            .email("email1")
            .passwordHash("passwordHash1")
            .firstName("firstName1")
            .lastName("lastName1")
            .phoneNumber("phoneNumber1")
            .keycloakUserId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .userAvatar("userAvatar1");
    }

    public static User getUserSample2() {
        return new User()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .username("username2")
            .email("email2")
            .passwordHash("passwordHash2")
            .firstName("firstName2")
            .lastName("lastName2")
            .phoneNumber("phoneNumber2")
            .keycloakUserId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .userAvatar("userAvatar2");
    }

    public static User getUserRandomSampleGenerator() {
        return new User()
            .id(UUID.randomUUID())
            .username(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .passwordHash(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .keycloakUserId(UUID.randomUUID())
            .userAvatar(UUID.randomUUID().toString());
    }
}
