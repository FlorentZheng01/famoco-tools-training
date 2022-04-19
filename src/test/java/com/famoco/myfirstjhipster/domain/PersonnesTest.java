package com.famoco.myfirstjhipster.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.famoco.myfirstjhipster.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonnesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personnes.class);
        Personnes personnes1 = new Personnes();
        personnes1.setId(1L);
        Personnes personnes2 = new Personnes();
        personnes2.setId(personnes1.getId());
        assertThat(personnes1).isEqualTo(personnes2);
        personnes2.setId(2L);
        assertThat(personnes1).isNotEqualTo(personnes2);
        personnes1.setId(null);
        assertThat(personnes1).isNotEqualTo(personnes2);
    }
}
