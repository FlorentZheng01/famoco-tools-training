package com.famoco.myfirstjhipster.web.rest;

import com.famoco.myfirstjhipster.domain.ODF;
import com.famoco.myfirstjhipster.repository.ODFRepository;
import com.famoco.myfirstjhipster.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.famoco.myfirstjhipster.domain.ODF}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ODFResource {

    private final Logger log = LoggerFactory.getLogger(ODFResource.class);

    private static final String ENTITY_NAME = "oDF";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ODFRepository oDFRepository;

    public ODFResource(ODFRepository oDFRepository) {
        this.oDFRepository = oDFRepository;
    }

    /**
     * {@code POST  /odfs} : Create a new oDF.
     *
     * @param oDF the oDF to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oDF, or with status {@code 400 (Bad Request)} if the oDF has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/odfs")
    public ResponseEntity<ODF> createODF(@RequestBody ODF oDF) throws URISyntaxException {
        log.debug("REST request to save ODF : {}", oDF);
        if (oDF.getId() != null) {
            throw new BadRequestAlertException("A new oDF cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ODF result = oDFRepository.save(oDF);
        return ResponseEntity
            .created(new URI("/api/odfs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /odfs/:id} : Updates an existing oDF.
     *
     * @param id the id of the oDF to save.
     * @param oDF the oDF to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oDF,
     * or with status {@code 400 (Bad Request)} if the oDF is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oDF couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/odfs/{id}")
    public ResponseEntity<ODF> updateODF(@PathVariable(value = "id", required = false) final String id, @RequestBody ODF oDF)
        throws URISyntaxException {
        log.debug("REST request to update ODF : {}, {}", id, oDF);
        if (oDF.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oDF.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oDFRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        oDF.setIsPersisted();
        ODF result = oDFRepository.save(oDF);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oDF.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /odfs/:id} : Partial updates given fields of an existing oDF, field will ignore if it is null
     *
     * @param id the id of the oDF to save.
     * @param oDF the oDF to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oDF,
     * or with status {@code 400 (Bad Request)} if the oDF is not valid,
     * or with status {@code 404 (Not Found)} if the oDF is not found,
     * or with status {@code 500 (Internal Server Error)} if the oDF couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/odfs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ODF> partialUpdateODF(@PathVariable(value = "id", required = false) final String id, @RequestBody ODF oDF)
        throws URISyntaxException {
        log.debug("REST request to partial update ODF partially : {}, {}", id, oDF);
        if (oDF.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oDF.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oDFRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ODF> result = oDFRepository
            .findById(oDF.getId())
            .map(existingODF -> {
                if (oDF.getStatus() != null) {
                    existingODF.setStatus(oDF.getStatus());
                }
                if (oDF.getOperationName() != null) {
                    existingODF.setOperationName(oDF.getOperationName());
                }
                if (oDF.getDateofcreation() != null) {
                    existingODF.setDateofcreation(oDF.getDateofcreation());
                }
                if (oDF.getDateofmodification() != null) {
                    existingODF.setDateofmodification(oDF.getDateofmodification());
                }
                if (oDF.getLastmodificationuser() != null) {
                    existingODF.setLastmodificationuser(oDF.getLastmodificationuser());
                }

                return existingODF;
            })
            .map(oDFRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oDF.getId()));
    }

    /**
     * {@code GET  /odfs} : get all the oDFS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oDFS in body.
     */
    @GetMapping("/odfs")
    public List<ODF> getAllODFS() {
        log.debug("REST request to get all ODFS");
        return oDFRepository.findAll();
    }

    /**
     * {@code GET  /odfs/:id} : get the "id" oDF.
     *
     * @param id the id of the oDF to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oDF, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/odfs/{id}")
    public ResponseEntity<ODF> getODF(@PathVariable String id) {
        log.debug("REST request to get ODF : {}", id);
        Optional<ODF> oDF = oDFRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(oDF);
    }

    /**
     * {@code DELETE  /odfs/:id} : delete the "id" oDF.
     *
     * @param id the id of the oDF to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/odfs/{id}")
    public ResponseEntity<Void> deleteODF(@PathVariable String id) {
        log.debug("REST request to delete ODF : {}", id);
        oDFRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
