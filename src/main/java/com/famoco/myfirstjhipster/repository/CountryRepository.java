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
    default Optional<Country> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Country> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Country> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct country from Country country left join fetch country.personne",
        countQuery = "select count(distinct country) from Country country"
    )
    Page<Country> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct country from Country country left join fetch country.personne")
    List<Country> findAllWithToOneRelationships();

    @Query("select country from Country country left join fetch country.personne where country.id =:id")
    Optional<Country> findOneWithToOneRelationships(@Param("id") Long id);
}
