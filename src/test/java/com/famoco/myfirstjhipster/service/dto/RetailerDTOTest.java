package com.famoco.myfirstjhipster.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.famoco.myfirstjhipster.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RetailerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RetailerDTO.class);
        RetailerDTO retailerDTO1 = new RetailerDTO();
        retailerDTO1.setId(1L);
        RetailerDTO retailerDTO2 = new RetailerDTO();
        assertThat(retailerDTO1).isNotEqualTo(retailerDTO2);
        retailerDTO2.setId(retailerDTO1.getId());
        assertThat(retailerDTO1).isEqualTo(retailerDTO2);
        retailerDTO2.setId(2L);
        assertThat(retailerDTO1).isNotEqualTo(retailerDTO2);
        retailerDTO1.setId(null);
        assertThat(retailerDTO1).isNotEqualTo(retailerDTO2);
    }
}
