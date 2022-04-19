package com.famoco.myfirstjhipster.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.famoco.myfirstjhipster.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileNameDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileNameDTO.class);
        FileNameDTO fileNameDTO1 = new FileNameDTO();
        fileNameDTO1.setId(1L);
        FileNameDTO fileNameDTO2 = new FileNameDTO();
        assertThat(fileNameDTO1).isNotEqualTo(fileNameDTO2);
        fileNameDTO2.setId(fileNameDTO1.getId());
        assertThat(fileNameDTO1).isEqualTo(fileNameDTO2);
        fileNameDTO2.setId(2L);
        assertThat(fileNameDTO1).isNotEqualTo(fileNameDTO2);
        fileNameDTO1.setId(null);
        assertThat(fileNameDTO1).isNotEqualTo(fileNameDTO2);
    }
}
