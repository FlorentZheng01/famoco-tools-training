//package com.famoco.myfirstjhipster.web.rest;
//
//import com.famoco.myfirstjhipster.domain.Personnes;
//import com.famoco.myfirstjhipster.repository.PersonnesRepository;
//import com.famoco.myfirstjhipster.web.rest.errors.BadRequestAlertException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//import tech.jhipster.web.util.HeaderUtil;
//import tech.jhipster.web.util.ResponseUtil;
//
///**
// * REST controller for managing {@link com.famoco.myfirstjhipster.domain.Personnes}.
// */
//@RestController
//@RequestMapping("/api")
//@Transactional
//public class PersonnesResource {
//
//    private final Logger log = LoggerFactory.getLogger(PersonnesResource.class);
//
//    private static final String ENTITY_NAME = "personnes";
//
//    @Value("${jhipster.clientApp.name}")
//    private String applicationName;
//
//    private final PersonnesRepository personnesRepository;
//
//    public PersonnesResource(PersonnesRepository personnesRepository) {
//        this.personnesRepository = personnesRepository;
//    }
//
//    /**
//     * {@code POST  /personnes} : Create a new personnes.
//     *
//     * @param personnes the personnes to create.
//     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personnes, or with status {@code 400 (Bad Request)} if the personnes has already an ID.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PostMapping("/personnes")
//    public ResponseEntity<Personnes> createPersonnes(@RequestBody Personnes personnes) throws URISyntaxException {
//        log.debug("REST request to save Personnes : {}", personnes);
//        if (personnes.getId() != null) {
//            throw new BadRequestAlertException("A new personnes cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        Personnes result = personnesRepository.save(personnes);
//        return ResponseEntity
//            .created(new URI("/api/personnes/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }
//
//    /**
//     * {@code PUT  /personnes/:id} : Updates an existing personnes.
//     *
//     * @param id the id of the personnes to save.
//     * @param personnes the personnes to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personnes,
//     * or with status {@code 400 (Bad Request)} if the personnes is not valid,
//     * or with status {@code 500 (Internal Server Error)} if the personnes couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PutMapping("/personnes/{id}")
//    public ResponseEntity<Personnes> updatePersonnes(
//        @PathVariable(value = "id", required = false) final Long id,
//        @RequestBody Personnes personnes
//    ) throws URISyntaxException {
//        log.debug("REST request to update Personnes : {}, {}", id, personnes);
//        if (personnes.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, personnes.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!personnesRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Personnes result = personnesRepository.save(personnes);
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personnes.getId().toString()))
//            .body(result);
//    }
//
//    /**
//     * {@code PATCH  /personnes/:id} : Partial updates given fields of an existing personnes, field will ignore if it is null
//     *
//     * @param id the id of the personnes to save.
//     * @param personnes the personnes to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personnes,
//     * or with status {@code 400 (Bad Request)} if the personnes is not valid,
//     * or with status {@code 404 (Not Found)} if the personnes is not found,
//     * or with status {@code 500 (Internal Server Error)} if the personnes couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PatchMapping(value = "/personnes/{id}", consumes = { "application/json", "application/merge-patch+json" })
//    public ResponseEntity<Personnes> partialUpdatePersonnes(
//        @PathVariable(value = "id", required = false) final Long id,
//        @RequestBody Personnes personnes
//    ) throws URISyntaxException {
//        log.debug("REST request to partial update Personnes partially : {}, {}", id, personnes);
//        if (personnes.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, personnes.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!personnesRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<Personnes> result = personnesRepository
//            .findById(personnes.getId())
//            .map(existingPersonnes -> {
//                if (personnes.getTest() != null) {
//                    existingPersonnes.setTest(personnes.getTest());
//                }
//
//                return existingPersonnes;
//            })
//            .map(personnesRepository::save);
//
//        return ResponseUtil.wrapOrNotFound(
//            result,
//            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personnes.getId().toString())
//        );
//    }
//
//    /**
//     * {@code GET  /personnes} : get all the personnes.
//     *
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personnes in body.
//     */
//    @GetMapping("/personnes")
//    public List<Personnes> getAllPersonnes() {
//        log.debug("REST request to get all Personnes");
//        return personnesRepository.findAll();
//    }
//
//    /**
//     * {@code GET  /personnes/:id} : get the "id" personnes.
//     *
//     * @param id the id of the personnes to retrieve.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personnes, or with status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/personnes/{id}")
//    public ResponseEntity<Personnes> getPersonnes(@PathVariable Long id) {
//        log.debug("REST request to get Personnes : {}", id);
//        Optional<Personnes> personnes = personnesRepository.findById(id);
//        return ResponseUtil.wrapOrNotFound(personnes);
//    }
//
//    /**
//     * {@code DELETE  /personnes/:id} : delete the "id" personnes.
//     *
//     * @param id the id of the personnes to delete.
//     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//     */
//    @DeleteMapping("/personnes/{id}")
//    public ResponseEntity<Void> deletePersonnes(@PathVariable Long id) {
//        log.debug("REST request to delete Personnes : {}", id);
//        personnesRepository.deleteById(id);
//        return ResponseEntity
//            .noContent()
//            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//            .build();
//    }
//}
