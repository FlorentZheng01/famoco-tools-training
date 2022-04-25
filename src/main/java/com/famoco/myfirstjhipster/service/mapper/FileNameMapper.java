package com.famoco.myfirstjhipster.service.mapper;

import com.famoco.myfirstjhipster.domain.FileName;
import com.famoco.myfirstjhipster.service.dto.FileNameDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FileName} and its DTO {@link FileNameDTO}.
 */
@Mapper(componentModel = "spring")
public interface FileNameMapper extends EntityMapper<FileNameDTO, FileName> {}
