package com.famoco.myfirstjhipster.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.famoco.myfirstjhipster.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileNameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileName.class);
        FileName fileName1 = new FileName();
        fileName1.setId(1L);
        FileName fileName2 = new FileName();
        fileName2.setId(fileName1.getId());
        assertThat(fileName1).isEqualTo(fileName2);
        fileName2.setId(2L);
        assertThat(fileName1).isNotEqualTo(fileName2);
        fileName1.setId(null);
        assertThat(fileName1).isNotEqualTo(fileName2);
    }
}
