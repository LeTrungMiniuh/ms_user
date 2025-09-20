package com.ridehub.user.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Profile getProfileSample1() {
        return new Profile().id(1L).fullName("fullName1").deletedBy(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Profile getProfileSample2() {
        return new Profile().id(2L).fullName("fullName2").deletedBy(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Profile getProfileRandomSampleGenerator() {
        return new Profile().id(longCount.incrementAndGet()).fullName(UUID.randomUUID().toString()).deletedBy(UUID.randomUUID());
    }
}
