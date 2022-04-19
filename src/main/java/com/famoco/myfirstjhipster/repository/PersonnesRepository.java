package com.famoco.myfirstjhipster.repository;

import com.famoco.myfirstjhipster.domain.Personnes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Personnes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonnesRepository extends JpaRepository<Personnes, Long> {}
