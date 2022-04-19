package com.famoco.myfirstjhipster.repository;

import com.famoco.myfirstjhipster.domain.ODF;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ODF entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ODFRepository extends JpaRepository<ODF, String> {}
