package com.famoco.myfirstjhipster.service.mapper;

import com.famoco.myfirstjhipster.domain.Retailer;
import com.famoco.myfirstjhipster.service.dto.RetailerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Retailer} and its DTO {@link RetailerDTO}.
 */
@Mapper(componentModel = "spring")
public interface RetailerMapper extends EntityMapper<RetailerDTO, Retailer> {}
