package com.famoco.myfirstjhipster.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.famoco.myfirstjhipster.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ODFTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ODF.class);
        ODF oDF1 = new ODF();
        oDF1.setId("id1");
        ODF oDF2 = new ODF();
        oDF2.setId(oDF1.getId());
        assertThat(oDF1).isEqualTo(oDF2);
        oDF2.setId("id2");
        assertThat(oDF1).isNotEqualTo(oDF2);
        oDF1.setId(null);
        assertThat(oDF1).isNotEqualTo(oDF2);
    }
}
