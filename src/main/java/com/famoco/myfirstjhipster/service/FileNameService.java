package com.famoco.myfirstjhipster.service;

import com.famoco.myfirstjhipster.domain.FileName;
import com.famoco.myfirstjhipster.repository.FileNameRepository;
import com.famoco.myfirstjhipster.service.dto.FileNameDTO;
import com.famoco.myfirstjhipster.service.mapper.FileNameMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link FileName}.
 */
@Service
@Transactional
public class FileNameService {

    private final Logger log = LoggerFactory.getLogger(FileNameService.class);

    private final FileNameRepository fileNameRepository;

    private final FileNameMapper fileNameMapper;

    public FileNameService(FileNameRepository fileNameRepository, FileNameMapper fileNameMapper) {
        this.fileNameRepository = fileNameRepository;
        this.fileNameMapper = fileNameMapper;
    }



    /**
     * Save a fileName.
     *
     * @param fileNameDTO the entity to save.
     * @return the persisted entity.
     */
    public FileNameDTO save(FileNameDTO fileNameDTO) {
        log.debug("Request to save FileName : {}", fileNameDTO);
        FileName fileName = fileNameMapper.toEntity(fileNameDTO);
        fileName = fileNameRepository.save(fileName);
        return fileNameMapper.toDto(fileName);
    }

    /**
     * Update a fileName.
     *ublic void setContent(byte[] content) {
        this.content =
     * @param fileNameDTO the entity to save.
     * @return the persisted entity.
     */
    public FileNameDTO update(FileNameDTO fileNameDTO) {
        log.debug("Request to save FileName : {}", fileNameDTO);
        FileName fileName = fileNameMapper.toEntity(fileNameDTO);
        fileName = fileNameRepository.save(fileName);
        return fileNameMapper.toDto(fileName);
    }

    /**
     * Partially update a fileName.
     *
     * @param fileNameDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FileNameDTO> partialUpdate(FileNameDTO fileNameDTO) {
        log.debug("Request to partially update FileName : {}", fileNameDTO);
x
        return fileNameRepository
            .findById(fileNameDTO.getId())
            .map(existingFileName -> {
                fileNameMapper.partialUpdate(existingFileName, fileNameDTO);

                return existingFileName;
            })
            .map(fileNameRepository::save)
            .map(fileNameMapper::toDto);
    }

    /**
     * Get all the fileNames.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FileNameDTO> findAll() {
        log.debug("Request to get all FileNames");
        return fileNameRepository.findAll().stream().map(fileNameMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one fileName by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FileNameDTO> findOne(Long id) {
        log.debug("Request to get FileName : {}", id);
        return fileNameRepository.findById(id).map(fileNameMapper::toDto);
    }

    /**
     * Delete the fileName by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FileName : {}", id);
        fileNameRepository.deleteById(id);
    }
}
