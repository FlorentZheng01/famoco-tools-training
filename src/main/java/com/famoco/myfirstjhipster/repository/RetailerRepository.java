package com.famoco.myfirstjhipster.repository;

import com.famoco.myfirstjhipster.domain.Retailer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Retailer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RetailerRepository extends JpaRepository<Retailer, Long>, JpaSpecificationExecutor<Retailer> {}
