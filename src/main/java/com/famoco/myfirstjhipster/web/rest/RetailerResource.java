package com.famoco.myfirstjhipster.web.rest;

import com.famoco.myfirstjhipster.repository.RetailerRepository;
import com.famoco.myfirstjhipster.service.RetailerQueryService;
import com.famoco.myfirstjhipster.service.RetailerService;
import com.famoco.myfirstjhipster.service.criteria.RetailerCriteria;
import com.famoco.myfirstjhipster.service.dto.RetailerDTO;
import com.famoco.myfirstjhipster.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.famoco.myfirstjhipster.domain.Retailer}.
 */
@RestController
@RequestMapping("/api")
public class RetailerResource {

    private final Logger log = LoggerFactory.getLogger(RetailerResource.class);

    private static final String ENTITY_NAME = "retailer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RetailerService retailerService;

    private final RetailerRepository retailerRepository;

    private final RetailerQueryService retailerQueryService;

    public RetailerResource(
        RetailerService retailerService,
        RetailerRepository retailerRepository,
        RetailerQueryService retailerQueryService
    ) {
        this.retailerService = retailerService;
        this.retailerRepository = retailerRepository;
        this.retailerQueryService = retailerQueryService;
    }

    /**
     * {@code POST  /retailers} : Create a new retailer.
     *
     * @param retailerDTO the retailerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new retailerDTO, or with status {@code 400 (Bad Request)} if the retailer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/retailers")
    public ResponseEntity<RetailerDTO> createRetailer(@Valid @RequestBody RetailerDTO retailerDTO) throws URISyntaxException {
        log.debug("REST request to save Retailer : {}", retailerDTO);
        if (retailerDTO.getId() != null) {
            throw new BadRequestAlertException("A new retailer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RetailerDTO result = retailerService.save(retailerDTO);
        return ResponseEntity
            .created(new URI("/api/retailers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /retailers/:id} : Updates an existing retailer.
     *
     * @param id the id of the retailerDTO to save.
     * @param retailerDTO the retailerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retailerDTO,
     * or with status {@code 400 (Bad Request)} if the retailerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the retailerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/retailers/{id}")
    public ResponseEntity<RetailerDTO> updateRetailer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RetailerDTO retailerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Retailer : {}, {}", id, retailerDTO);
        if (retailerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, retailerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retailerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RetailerDTO result = retailerService.update(retailerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retailerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /retailers/:id} : Partial updates given fields of an existing retailer, field will ignore if it is null
     *
     * @param id the id of the retailerDTO to save.
     * @param retailerDTO the retailerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retailerDTO,
     * or with status {@code 400 (Bad Request)} if the retailerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the retailerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the retailerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/retailers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RetailerDTO> partialUpdateRetailer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RetailerDTO retailerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Retailer partially : {}, {}", id, retailerDTO);
        if (retailerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, retailerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retailerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RetailerDTO> result = retailerService.partialUpdate(retailerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retailerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /retailers} : get all the retailers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of retailers in body.
     */
    @GetMapping("/retailers")
    public ResponseEntity<List<RetailerDTO>> getAllRetailers(
        RetailerCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Retailers by criteria: {}", criteria);
        Page<RetailerDTO> page = retailerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /retailers/count} : count all the retailers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/retailers/count")
    public ResponseEntity<Long> countRetailers(RetailerCriteria criteria) {
        log.debug("REST request to count Retailers by criteria: {}", criteria);
        return ResponseEntity.ok().body(retailerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /retailers/:id} : get the "id" retailer.
     *
     * @param id the id of the retailerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the retailerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/retailers/{id}")
    public ResponseEntity<RetailerDTO> getRetailer(@PathVariable Long id) {
        log.debug("REST request to get Retailer : {}", id);
        Optional<RetailerDTO> retailerDTO = retailerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(retailerDTO);
    }

    /**
     * {@code DELETE  /retailers/:id} : delete the "id" retailer.
     *
     * @param id the id of the retailerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/retailers/{id}")
    public ResponseEntity<Void> deleteRetailer(@PathVariable Long id) {
        log.debug("REST request to delete Retailer : {}", id);
        retailerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
