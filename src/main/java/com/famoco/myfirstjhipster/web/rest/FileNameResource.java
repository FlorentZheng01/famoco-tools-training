package com.famoco.myfirstjhipster.web.rest;

import com.famoco.myfirstjhipster.repository.FileNameRepository;
import com.famoco.myfirstjhipster.service.FileNameService;
import com.famoco.myfirstjhipster.service.dto.FileNameDTO;
import com.famoco.myfirstjhipster.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * REST controller for managing {@link com.famoco.myfirstjhipster.domain.FileName}.
 */
@RestController
@RequestMapping("/api")
public class FileNameResource {

    private final Logger log = LoggerFactory.getLogger(FileNameResource.class);

    private static final String ENTITY_NAME = "fileName";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileNameService fileNameService;

    private final FileNameRepository fileNameRepository;


    public FileNameResource(FileNameService fileNameService, FileNameRepository fileNameRepository) {
        this.fileNameService = fileNameService;
        this.fileNameRepository = fileNameRepository;
    }




    /**
     * {@code POST  /file-names} : Create a new fileName.
     *
     * @param fileNameDTO the fileNameDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileNameDTO, or with status {@code 400 (Bad Request)} if the fileName has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-names")
    public ResponseEntity<FileNameDTO> createFileName(@RequestBody FileNameDTO fileNameDTO) throws URISyntaxException {
        log.debug("REST request to save FileName : {}", fileNameDTO);
        if (fileNameDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileName cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileNameDTO result = fileNameService.save(fileNameDTO);
        return ResponseEntity
            .created(new URI("/api/file-names/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);

    }



    /**
     * {@code PUT  /file-names/:id} : Updates an existing fileName.
     *
     * @param id the id of the fileNameDTO to save.
     * @param fileNameDTO the fileNameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileNameDTO,
     * or with status {@code 400 (Bad Request)} if the fileNameDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileNameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-names/{id}")
    public ResponseEntity<FileNameDTO> updateFileName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FileNameDTO fileNameDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FileName : {}, {}", id, fileNameDTO);
        if (fileNameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileNameDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FileNameDTO result = fileNameService.update(fileNameDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileNameDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /file-names/:id} : Partial updates given fields of an existing fileName, field will ignore if it is null
     *
     * @param id the id of the fileNameDTO to save.
     * @param fileNameDTO the fileNameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileNameDTO,
     * or with status {@code 400 (Bad Request)} if the fileNameDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fileNameDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileNameDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/file-names/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FileNameDTO> partialUpdateFileName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FileNameDTO fileNameDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FileName partially : {}, {}", id, fileNameDTO);
        if (fileNameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileNameDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileNameDTO> result = fileNameService.partialUpdate(fileNameDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileNameDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /file-names} : get all the fileNames.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileNames in body.
     */
    @GetMapping("/file-names")
    public List<FileNameDTO> getAllFileNames() {
        log.debug("REST request to get all FileNames");
        return fileNameService.findAll();
    }

    /**
     * {@code GET  /file-names/:id} : get the "id" fileName.
     *
     * @param id the id of the fileNameDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileNameDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-names/{id}")
    public ResponseEntity<FileNameDTO> getFileName(@PathVariable Long id) {
        log.debug("REST request to get FileName : {}", id);
        Optional<FileNameDTO> fileNameDTO = fileNameService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileNameDTO);
    }

    /**
     * {@code DELETE  /file-names/:id} : delete the "id" fileName.
     *
     * @param id the id of the fileNameDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-names/{id}")
    public ResponseEntity<Void> deleteFileName(@PathVariable Long id) {
        log.debug("REST request to delete FileName : {}", id);
        fileNameService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
