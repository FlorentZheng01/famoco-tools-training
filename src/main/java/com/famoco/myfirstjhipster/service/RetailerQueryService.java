package com.famoco.myfirstjhipster.service;

import com.famoco.myfirstjhipster.domain.*; // for static metamodels
import com.famoco.myfirstjhipster.domain.Retailer;
import com.famoco.myfirstjhipster.repository.RetailerRepository;
import com.famoco.myfirstjhipster.service.criteria.RetailerCriteria;
import com.famoco.myfirstjhipster.service.dto.RetailerDTO;
import com.famoco.myfirstjhipster.service.mapper.RetailerMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Retailer} entities in the database.
 * The main input is a {@link RetailerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RetailerDTO} or a {@link Page} of {@link RetailerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RetailerQueryService extends QueryService<Retailer> {

    private final Logger log = LoggerFactory.getLogger(RetailerQueryService.class);

    private final RetailerRepository retailerRepository;

    private final RetailerMapper retailerMapper;

    public RetailerQueryService(RetailerRepository retailerRepository, RetailerMapper retailerMapper) {
        this.retailerRepository = retailerRepository;
        this.retailerMapper = retailerMapper;
    }

    /**
     * Return a {@link List} of {@link RetailerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RetailerDTO> findByCriteria(RetailerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Retailer> specification = createSpecification(criteria);
        return retailerMapper.toDto(retailerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RetailerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RetailerDTO> findByCriteria(RetailerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Retailer> specification = createSpecification(criteria);
        return retailerRepository.findAll(specification, page).map(retailerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RetailerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Retailer> specification = createSpecification(criteria);
        return retailerRepository.count(specification);
    }

    /**
     * Function to convert {@link RetailerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Retailer> createSpecification(RetailerCriteria criteria) {
        Specification<Retailer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Retailer_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Retailer_.name));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPhoneNumber(), Retailer_.phoneNumber));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Retailer_.address));
            }
            if (criteria.getMail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMail(), Retailer_.mail));
            }
        }
        return specification;
    }
}
