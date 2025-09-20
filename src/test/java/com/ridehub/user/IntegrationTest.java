package com.ridehub.user;

import com.ridehub.user.config.AsyncSyncConfiguration;
import com.ridehub.user.config.EmbeddedRedis;
import com.ridehub.user.config.EmbeddedSQL;
import com.ridehub.user.config.JacksonConfiguration;
import com.ridehub.user.config.TestSecurityConfiguration;
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
