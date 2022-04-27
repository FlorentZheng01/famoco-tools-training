package com.famoco.myfirstjhipster.repository;

import com.famoco.myfirstjhipster.domain.Country;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Country entity.
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {


}
