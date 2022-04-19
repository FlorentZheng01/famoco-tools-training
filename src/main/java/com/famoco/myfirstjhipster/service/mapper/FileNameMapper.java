package com.famoco.myfirstjhipster.service.mapper;

import com.famoco.myfirstjhipster.domain.FileName;
import com.famoco.myfirstjhipster.service.dto.FileNameDTO;
import org.mapstruct.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;
/**
 * Mapper for the entity {@link FileName} and its DTO {@link FileNameDTO}.
 */
@Mapper(componentModel = "spring")
public interface FileNameMapper extends EntityMapper<FileNameDTO, FileName> {



}
