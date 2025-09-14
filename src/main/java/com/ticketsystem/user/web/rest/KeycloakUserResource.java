package com.ticketsystem.user.web.rest;

import com.ticketsystem.user.repository.KeycloakUserRepository;
import com.ticketsystem.user.service.KeycloakUserService;
import com.ticketsystem.user.service.dto.KeycloakUserDTO;
import com.ticketsystem.user.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ticketsystem.user.domain.KeycloakUser}.
 */
@RestController
@RequestMapping("/api/keycloak-users")
public class KeycloakUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakUserResource.class);

    private static final String ENTITY_NAME = "msUserKeycloakUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KeycloakUserService keycloakUserService;

    private final KeycloakUserRepository keycloakUserRepository;

    public KeycloakUserResource(KeycloakUserService keycloakUserService, KeycloakUserRepository keycloakUserRepository) {
        this.keycloakUserService = keycloakUserService;
        this.keycloakUserRepository = keycloakUserRepository;
    }

    /**
     * {@code POST  /keycloak-users} : Create a new keycloakUser.
     *
     * @param keycloakUserDTO the keycloakUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new keycloakUserDTO, or with status {@code 400 (Bad Request)} if the keycloakUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<KeycloakUserDTO> createKeycloakUser(@Valid @RequestBody KeycloakUserDTO keycloakUserDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save KeycloakUser : {}", keycloakUserDTO);
        if (keycloakUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new keycloakUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        keycloakUserDTO = keycloakUserService.save(keycloakUserDTO);
        return ResponseEntity.created(new URI("/api/keycloak-users/" + keycloakUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, keycloakUserDTO.getId().toString()))
            .body(keycloakUserDTO);
    }

    /**
     * {@code PUT  /keycloak-users/:id} : Updates an existing keycloakUser.
     *
     * @param id the id of the keycloakUserDTO to save.
     * @param keycloakUserDTO the keycloakUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated keycloakUserDTO,
     * or with status {@code 400 (Bad Request)} if the keycloakUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the keycloakUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<KeycloakUserDTO> updateKeycloakUser(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody KeycloakUserDTO keycloakUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update KeycloakUser : {}, {}", id, keycloakUserDTO);
        if (keycloakUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, keycloakUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!keycloakUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        keycloakUserDTO = keycloakUserService.update(keycloakUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, keycloakUserDTO.getId().toString()))
            .body(keycloakUserDTO);
    }

    /**
     * {@code PATCH  /keycloak-users/:id} : Partial updates given fields of an existing keycloakUser, field will ignore if it is null
     *
     * @param id the id of the keycloakUserDTO to save.
     * @param keycloakUserDTO the keycloakUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated keycloakUserDTO,
     * or with status {@code 400 (Bad Request)} if the keycloakUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the keycloakUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the keycloakUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KeycloakUserDTO> partialUpdateKeycloakUser(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody KeycloakUserDTO keycloakUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update KeycloakUser partially : {}, {}", id, keycloakUserDTO);
        if (keycloakUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, keycloakUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!keycloakUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KeycloakUserDTO> result = keycloakUserService.partialUpdate(keycloakUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, keycloakUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /keycloak-users} : get all the keycloakUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of keycloakUsers in body.
     */
    @GetMapping("")
    public List<KeycloakUserDTO> getAllKeycloakUsers() {
        LOG.debug("REST request to get all KeycloakUsers");
        return keycloakUserService.findAll();
    }

    /**
     * {@code GET  /keycloak-users/:id} : get the "id" keycloakUser.
     *
     * @param id the id of the keycloakUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the keycloakUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KeycloakUserDTO> getKeycloakUser(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get KeycloakUser : {}", id);
        Optional<KeycloakUserDTO> keycloakUserDTO = keycloakUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(keycloakUserDTO);
    }

    /**
     * {@code DELETE  /keycloak-users/:id} : delete the "id" keycloakUser.
     *
     * @param id the id of the keycloakUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeycloakUser(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete KeycloakUser : {}", id);
        keycloakUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
