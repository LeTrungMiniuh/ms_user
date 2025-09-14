package com.ticketsystem.user;

import com.ticketsystem.user.config.AsyncSyncConfiguration;
import com.ticketsystem.user.config.EmbeddedRedis;
import com.ticketsystem.user.config.EmbeddedSQL;
import com.ticketsystem.user.config.JacksonConfiguration;
import com.ticketsystem.user.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { MsUserApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
public @interface IntegrationTest {
}
