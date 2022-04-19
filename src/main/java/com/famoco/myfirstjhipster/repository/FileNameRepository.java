package com.famoco.myfirstjhipster.repository;

import com.famoco.myfirstjhipster.domain.FileName;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FileName entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileNameRepository extends JpaRepository<FileName, Long> {}
