package com.famoco.myfirstjhipster.service.mapper;

import com.famoco.myfirstjhipster.domain.Device;
import com.famoco.myfirstjhipster.domain.Retailer;
import com.famoco.myfirstjhipster.service.dto.DeviceDTO;
import com.famoco.myfirstjhipster.service.dto.RetailerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {
    @Mapping(target = "retailer", source = "retailer", qualifiedByName = "retailerId")
    DeviceDTO toDto(Device s);

    @Named("retailerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RetailerDTO toDtoRetailerId(Retailer retailer);
}
