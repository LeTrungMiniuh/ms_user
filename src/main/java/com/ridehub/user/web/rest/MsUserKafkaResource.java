package com.ridehub.user.web.rest;

import com.ridehub.user.broker.KafkaProducer;
import com.ridehub.user.service.dto.AppUserDTO;
import com.ridehub.kafka.service.KafkaUtilityService;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.RetriableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ms-user-kafka")
public class MsUserKafkaResource {

    private static final Logger LOG = LoggerFactory.getLogger(MsUserKafkaResource.class);

    private final KafkaProducer kafkaProducer; // now only has get() + send()
    private final KafkaUtilityService kafkaUtilityService;

    public MsUserKafkaResource(
            KafkaProducer kafkaProducer,
            KafkaUtilityService kafkaUtilityService) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaUtilityService = kafkaUtilityService;
    }

    /**
     * Test endpoint to send a simple message
     */
    @PostMapping("/publish/simple")
    public ResponseEntity<Map<String, String>> publishSimple(@RequestParam("message") String message) {
        LOG.debug("REST request to send simple message: {} to Kafka via Supplier", message);

        try {
            String messageKey = kafkaProducer.send("user.test", message);
            if (messageKey == null) {
                throw new IllegalStateException("Failed to queue event (null key returned)");
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "Message queued successfully");
            response.put("messageKey", messageKey);
            response.put("payload", message);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error queueing simple message: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to queue message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Send a user creation event
     */
    @PostMapping("/publish/user-created")
    public ResponseEntity<Map<String, String>> publishUserCreated(@RequestBody AppUserDTO AppUserDTO) {
        LOG.debug("REST request to queue user created event for: {}", AppUserDTO);

        try {
            String messageKey = kafkaProducer.send("user.created", AppUserDTO);
            if (messageKey == null) {
                throw new IllegalStateException("Failed to queue event (null key returned)");
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "User created event queued successfully");
            response.put("messageKey", messageKey);
            response.put("userId", String.valueOf(AppUserDTO.getId()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error queueing user created event: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to queue user created event: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Send a user updated event
     */
    @PostMapping("/publish/user-updated")
    public ResponseEntity<Map<String, String>> publishUserUpdated(@RequestBody AppUserDTO AppUserDTO) {
        LOG.debug("REST request to queue user updated event for: {}", AppUserDTO);
        try {
            String messageKey = kafkaProducer.send("user.updated", AppUserDTO);
            if (messageKey == null) {
                throw new IllegalStateException("Failed to queue event (null key returned)");
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "User updated event queued successfully");
            response.put("messageKey", messageKey);
            response.put("userId", String.valueOf(AppUserDTO.getId()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error queueing user updated event: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to queue user updated event: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }

    }

    /**
     * Send a user deleted event
     */
    @DeleteMapping("/publish/user-deleted/{id}")
    public ResponseEntity<Map<String, String>> publishUserDeleted(@PathVariable Long id) {
        LOG.debug("REST request to queue user deleted event for ID: {}", id);

        try {
            String messageKey = kafkaProducer.send("user.deleted", id);
            if (messageKey == null) {
                throw new IllegalStateException("Failed to queue event (null key returned)");
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted event queued successfully");
            response.put("messageKey", messageKey);
            response.put("userId", String.valueOf(id));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error queueing user deleted event: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to queue user deleted event: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Get Kafka utility service status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "ms_user");
        status.put("kafkaUtilityEnabled", true);
        status.put("sseClientsCount", kafkaUtilityService.getEmitters().size());
        status.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(status);
    }
}
