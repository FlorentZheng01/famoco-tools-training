package com.famoco.myfirstjhipster.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.famoco.myfirstjhipster.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RetailerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Retailer.class);
        Retailer retailer1 = new Retailer();
        retailer1.setId(1L);
        Retailer retailer2 = new Retailer();
        retailer2.setId(retailer1.getId());
        assertThat(retailer1).isEqualTo(retailer2);
        retailer2.setId(2L);
        assertThat(retailer1).isNotEqualTo(retailer2);
        retailer1.setId(null);
        assertThat(retailer1).isNotEqualTo(retailer2);
    }
}
