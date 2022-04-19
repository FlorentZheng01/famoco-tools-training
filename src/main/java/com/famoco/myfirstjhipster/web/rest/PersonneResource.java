package com.famoco.myfirstjhipster.web.rest;

import com.famoco.myfirstjhipster.domain.Personne;
import com.famoco.myfirstjhipster.repository.PersonneRepository;
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
 * REST controller for managing {@link com.famoco.myfirstjhipster.domain.Personne}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PersonneResource {

    private final Logger log = LoggerFactory.getLogger(PersonneResource.class);

    private final PersonneRepository personneRepository;

    public PersonneResource(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    /**
     * {@code GET  /personnes} : get all the personnes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personnes in body.
     */
    @GetMapping("/personnes")
    public List<Personne> getAllPersonnes() {
        log.debug("REST request to get all Personnes");
        return personneRepository.findAll();
    }

    /**
     * {@code GET  /personnes/:id} : get the "id" personne.
     *
     * @param id the id of the personne to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personne, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personnes/{id}")
    public ResponseEntity<Personne> getPersonne(@PathVariable Long id) {
        log.debug("REST request to get Personne : {}", id);
        Optional<Personne> personne = personneRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(personne);
    }
}
