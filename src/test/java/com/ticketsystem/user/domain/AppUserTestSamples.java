package com.ticketsystem.user.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppUser getAppUserSample1() {
        return new AppUser()
            .id(1L)
            .username("username1")
            .email("email1")
            .phoneNumber("phoneNumber1")
            .firstName("firstName1")
            .lastName("lastName1")
            .idNumber("idNumber1")
            .nationality("nationality1")
            .profileImage("profileImage1");
    }

    public static AppUser getAppUserSample2() {
        return new AppUser()
            .id(2L)
            .username("username2")
            .email("email2")
            .phoneNumber("phoneNumber2")
            .firstName("firstName2")
            .lastName("lastName2")
            .idNumber("idNumber2")
            .nationality("nationality2")
            .profileImage("profileImage2");
    }

    public static AppUser getAppUserRandomSampleGenerator() {
        return new AppUser()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .idNumber(UUID.randomUUID().toString())
            .nationality(UUID.randomUUID().toString())
            .profileImage(UUID.randomUUID().toString());
    }
}
