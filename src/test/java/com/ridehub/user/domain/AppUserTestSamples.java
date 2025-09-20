package com.ridehub.user.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppUser getAppUserSample1() {
        return new AppUser()
            .id(1L)
            .keycloakId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .email("email1")
            .phoneNumber("phoneNumber1")
            .firstName("firstName1")
            .lastName("lastName1")
            .deletedBy(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static AppUser getAppUserSample2() {
        return new AppUser()
            .id(2L)
            .keycloakId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .email("email2")
            .phoneNumber("phoneNumber2")
            .firstName("firstName2")
            .lastName("lastName2")
            .deletedBy(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static AppUser getAppUserRandomSampleGenerator() {
        return new AppUser()
            .id(longCount.incrementAndGet())
            .keycloakId(UUID.randomUUID())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .deletedBy(UUID.randomUUID());
    }
}
