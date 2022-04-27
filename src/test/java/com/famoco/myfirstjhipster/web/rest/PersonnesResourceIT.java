package com.famoco.myfirstjhipster.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.famoco.myfirstjhipster.IntegrationTest;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PersonnesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonnesResourceIT {

    private static final String DEFAULT_TEST = "AAAAAAAAAA";
    private static final String UPDATED_TEST = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonnesRepository personnesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonnesMockMvc;

    private Personnes personnes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personnes createEntity(EntityManager em) {
        Personnes personnes = new Personnes().test(DEFAULT_TEST);
        return personnes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personnes createUpdatedEntity(EntityManager em) {
        Personnes personnes = new Personnes().test(UPDATED_TEST);
        return personnes;
    }

    @BeforeEach
    public void initTest() {
        personnes = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonnes() throws Exception {
        int databaseSizeBeforeCreate = personnesRepository.findAll().size();
        // Create the Personnes
        restPersonnesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnes)))
            .andExpect(status().isCreated());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeCreate + 1);
        Personnes testPersonnes = personnesList.get(personnesList.size() - 1);
        assertThat(testPersonnes.getTest()).isEqualTo(DEFAULT_TEST);
    }

    @Test
    @Transactional
    void createPersonnesWithExistingId() throws Exception {
        // Create the Personnes with an existing ID
        personnes.setId(1L);

        int databaseSizeBeforeCreate = personnesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonnesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnes)))
            .andExpect(status().isBadRequest());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonnes() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get all the personnesList
        restPersonnesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personnes.getId().intValue())))
            .andExpect(jsonPath("$.[*].test").value(hasItem(DEFAULT_TEST)));
    }

    @Test
    @Transactional
    void getPersonnes() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        // Get the personnes
        restPersonnesMockMvc
            .perform(get(ENTITY_API_URL_ID, personnes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personnes.getId().intValue()))
            .andExpect(jsonPath("$.test").value(DEFAULT_TEST));
    }

    @Test
    @Transactional
    void getNonExistingPersonnes() throws Exception {
        // Get the personnes
        restPersonnesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonnes() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        int databaseSizeBeforeUpdate = personnesRepository.findAll().size();

        // Update the personnes
        Personnes updatedPersonnes = personnesRepository.findById(personnes.getId()).get();
        // Disconnect from session so that the updates on updatedPersonnes are not directly saved in db
        em.detach(updatedPersonnes);
        updatedPersonnes.test(UPDATED_TEST);

        restPersonnesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonnes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonnes))
            )
            .andExpect(status().isOk());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeUpdate);
        Personnes testPersonnes = personnesList.get(personnesList.size() - 1);
        assertThat(testPersonnes.getTest()).isEqualTo(UPDATED_TEST);
    }

    @Test
    @Transactional
    void putNonExistingPersonnes() throws Exception {
        int databaseSizeBeforeUpdate = personnesRepository.findAll().size();
        personnes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonnesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personnes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personnes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonnes() throws Exception {
        int databaseSizeBeforeUpdate = personnesRepository.findAll().size();
        personnes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personnes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonnes() throws Exception {
        int databaseSizeBeforeUpdate = personnesRepository.findAll().size();
        personnes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonnesWithPatch() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        int databaseSizeBeforeUpdate = personnesRepository.findAll().size();

        // Update the personnes using partial update
        Personnes partialUpdatedPersonnes = new Personnes();
        partialUpdatedPersonnes.setId(personnes.getId());

        partialUpdatedPersonnes.test(UPDATED_TEST);

        restPersonnesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonnes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonnes))
            )
            .andExpect(status().isOk());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeUpdate);
        Personnes testPersonnes = personnesList.get(personnesList.size() - 1);
        assertThat(testPersonnes.getTest()).isEqualTo(UPDATED_TEST);
    }

    @Test
    @Transactional
    void fullUpdatePersonnesWithPatch() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        int databaseSizeBeforeUpdate = personnesRepository.findAll().size();

        // Update the personnes using partial update
        Personnes partialUpdatedPersonnes = new Personnes();
        partialUpdatedPersonnes.setId(personnes.getId());

        partialUpdatedPersonnes.test(UPDATED_TEST);

        restPersonnesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonnes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonnes))
            )
            .andExpect(status().isOk());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeUpdate);
        Personnes testPersonnes = personnesList.get(personnesList.size() - 1);
        assertThat(testPersonnes.getTest()).isEqualTo(UPDATED_TEST);
    }

    @Test
    @Transactional
    void patchNonExistingPersonnes() throws Exception {
        int databaseSizeBeforeUpdate = personnesRepository.findAll().size();
        personnes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonnesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personnes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personnes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonnes() throws Exception {
        int databaseSizeBeforeUpdate = personnesRepository.findAll().size();
        personnes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personnes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonnes() throws Exception {
        int databaseSizeBeforeUpdate = personnesRepository.findAll().size();
        personnes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personnes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personnes in the database
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonnes() throws Exception {
        // Initialize the database
        personnesRepository.saveAndFlush(personnes);

        int databaseSizeBeforeDelete = personnesRepository.findAll().size();

        // Delete the personnes
        restPersonnesMockMvc
            .perform(delete(ENTITY_API_URL_ID, personnes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personnes> personnesList = personnesRepository.findAll();
        assertThat(personnesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
