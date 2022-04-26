package com.famoco.myfirstjhipster.service;

import com.famoco.myfirstjhipster.domain.Retailer;
import com.famoco.myfirstjhipster.repository.RetailerRepository;
import com.famoco.myfirstjhipster.service.dto.RetailerDTO;
import com.famoco.myfirstjhipster.service.mapper.RetailerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Retailer}.
 */
@Service
@Transactional
public class RetailerService {

    private final Logger log = LoggerFactory.getLogger(RetailerService.class);

    private final RetailerRepository retailerRepository;

    private final RetailerMapper retailerMapper;

    public RetailerService(RetailerRepository retailerRepository, RetailerMapper retailerMapper) {
        this.retailerRepository = retailerRepository;
        this.retailerMapper = retailerMapper;
    }

    /**
     * Save a retailer.
     *
     * @param retailerDTO the entity to save.
     * @return the persisted entity.
     */
    public RetailerDTO save(RetailerDTO retailerDTO) {
        log.debug("Request to save Retailer : {}", retailerDTO);
        Retailer retailer = retailerMapper.toEntity(retailerDTO);
        retailer = retailerRepository.save(retailer);
        return retailerMapper.toDto(retailer);
    }

    /**
     * Update a retailer.
     *
     * @param retailerDTO the entity to save.
     * @return the persisted entity.
     */
    public RetailerDTO update(RetailerDTO retailerDTO) {
        log.debug("Request to save Retailer : {}", retailerDTO);
        Retailer retailer = retailerMapper.toEntity(retailerDTO);
        retailer = retailerRepository.save(retailer);
        return retailerMapper.toDto(retailer);
    }

    /**
     * Partially update a retailer.
     *
     * @param retailerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RetailerDTO> partialUpdate(RetailerDTO retailerDTO) {
        log.debug("Request to partially update Retailer : {}", retailerDTO);

        return retailerRepository
            .findById(retailerDTO.getId())
            .map(existingRetailer -> {
                retailerMapper.partialUpdate(existingRetailer, retailerDTO);

                return existingRetailer;
            })
            .map(retailerRepository::save)
            .map(retailerMapper::toDto);
    }

    /**
     * Get all the retailers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RetailerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Retailers");
        return retailerRepository.findAll(pageable).map(retailerMapper::toDto);
    }

    /**
     * Get one retailer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RetailerDTO> findOne(Long id) {
        log.debug("Request to get Retailer : {}", id);
        return retailerRepository.findById(id).map(retailerMapper::toDto);
    }

    /**
     * Delete the retailer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Retailer : {}", id);
        retailerRepository.deleteById(id);
    }
}
