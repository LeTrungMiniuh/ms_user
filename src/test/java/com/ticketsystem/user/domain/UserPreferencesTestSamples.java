package com.ticketsystem.user.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserPreferencesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserPreferences getUserPreferencesSample1() {
        return new UserPreferences().id(1L).preferredLanguage("preferredLanguage1").phone("phone1").email("email1");
    }

    public static UserPreferences getUserPreferencesSample2() {
        return new UserPreferences().id(2L).preferredLanguage("preferredLanguage2").phone("phone2").email("email2");
    }

    public static UserPreferences getUserPreferencesRandomSampleGenerator() {
        return new UserPreferences()
            .id(longCount.incrementAndGet())
            .preferredLanguage(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
