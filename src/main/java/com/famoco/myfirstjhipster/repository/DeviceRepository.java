package com.famoco.myfirstjhipster.repository;

import com.famoco.myfirstjhipster.domain.Country;
import com.famoco.myfirstjhipster.domain.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Device entity.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {

    @Query("select device from Device device where device.macAddress like concat('%', ?1 ,'%') ")
    Page<Device> findWithSearchPageable(String search,Pageable pageable);

    @Query("select device " +
        " from Device device " +
        " left join device.retailer retailer " +
        " where (retailer.id = ?2) or (?2 is null) and device.macAddress like concat('%', ?1 ,'%')  ")
    Page<Device> findWithRetailerSearchPageable(String search, Long retailerId, Pageable pageable);


}
