package com.famoco.myfirstjhipster.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.famoco.myfirstjhipster.IntegrationTest;
import com.famoco.myfirstjhipster.domain.ODF;
import com.famoco.myfirstjhipster.repository.ODFRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
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
 * Integration tests for the {@link ODFResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ODFResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_OPERATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OPERATION_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATEOFCREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEOFCREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATEOFMODIFICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEOFMODIFICATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LASTMODIFICATIONUSER = "AAAAAAAAAA";
    private static final String UPDATED_LASTMODIFICATIONUSER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/odfs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ODFRepository oDFRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restODFMockMvc;

    private ODF oDF;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ODF createEntity(EntityManager em) {
        ODF oDF = new ODF()
            .status(DEFAULT_STATUS)
            .operationName(DEFAULT_OPERATION_NAME)
            .dateofcreation(DEFAULT_DATEOFCREATION)
            .dateofmodification(DEFAULT_DATEOFMODIFICATION)
            .lastmodificationuser(DEFAULT_LASTMODIFICATIONUSER);
        return oDF;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ODF createUpdatedEntity(EntityManager em) {
        ODF oDF = new ODF()
            .status(UPDATED_STATUS)
            .operationName(UPDATED_OPERATION_NAME)
            .dateofcreation(UPDATED_DATEOFCREATION)
            .dateofmodification(UPDATED_DATEOFMODIFICATION)
            .lastmodificationuser(UPDATED_LASTMODIFICATIONUSER);
        return oDF;
    }

    @BeforeEach
    public void initTest() {
        oDF = createEntity(em);
    }

    @Test
    @Transactional
    void createODF() throws Exception {
        int databaseSizeBeforeCreate = oDFRepository.findAll().size();
        // Create the ODF
        restODFMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oDF)))
            .andExpect(status().isCreated());

        // Validate the ODF in the database
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeCreate + 1);
        ODF testODF = oDFList.get(oDFList.size() - 1);
        assertThat(testODF.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testODF.getOperationName()).isEqualTo(DEFAULT_OPERATION_NAME);
        assertThat(testODF.getDateofcreation()).isEqualTo(DEFAULT_DATEOFCREATION);
        assertThat(testODF.getDateofmodification()).isEqualTo(DEFAULT_DATEOFMODIFICATION);
        assertThat(testODF.getLastmodificationuser()).isEqualTo(DEFAULT_LASTMODIFICATIONUSER);
    }

    @Test
    @Transactional
    void createODFWithExistingId() throws Exception {
        // Create the ODF with an existing ID
        oDF.setId("existing_id");

        int databaseSizeBeforeCreate = oDFRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restODFMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oDF)))
            .andExpect(status().isBadRequest());

        // Validate the ODF in the database
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllODFS() throws Exception {
        // Initialize the database
        oDF.setId(UUID.randomUUID().toString());
        oDFRepository.saveAndFlush(oDF);

        // Get all the oDFList
        restODFMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oDF.getId())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].operationName").value(hasItem(DEFAULT_OPERATION_NAME)))
            .andExpect(jsonPath("$.[*].dateofcreation").value(hasItem(DEFAULT_DATEOFCREATION.toString())))
            .andExpect(jsonPath("$.[*].dateofmodification").value(hasItem(DEFAULT_DATEOFMODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].lastmodificationuser").value(hasItem(DEFAULT_LASTMODIFICATIONUSER)));
    }

    @Test
    @Transactional
    void getODF() throws Exception {
        // Initialize the database
        oDF.setId(UUID.randomUUID().toString());
        oDFRepository.saveAndFlush(oDF);

        // Get the oDF
        restODFMockMvc
            .perform(get(ENTITY_API_URL_ID, oDF.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oDF.getId()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.operationName").value(DEFAULT_OPERATION_NAME))
            .andExpect(jsonPath("$.dateofcreation").value(DEFAULT_DATEOFCREATION.toString()))
            .andExpect(jsonPath("$.dateofmodification").value(DEFAULT_DATEOFMODIFICATION.toString()))
            .andExpect(jsonPath("$.lastmodificationuser").value(DEFAULT_LASTMODIFICATIONUSER));
    }

    @Test
    @Transactional
    void getNonExistingODF() throws Exception {
        // Get the oDF
        restODFMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewODF() throws Exception {
        // Initialize the database
        oDF.setId(UUID.randomUUID().toString());
        oDFRepository.saveAndFlush(oDF);

        int databaseSizeBeforeUpdate = oDFRepository.findAll().size();

        // Update the oDF
        ODF updatedODF = oDFRepository.findById(oDF.getId()).get();
        // Disconnect from session so that the updates on updatedODF are not directly saved in db
        em.detach(updatedODF);
        updatedODF
            .status(UPDATED_STATUS)
            .operationName(UPDATED_OPERATION_NAME)
            .dateofcreation(UPDATED_DATEOFCREATION)
            .dateofmodification(UPDATED_DATEOFMODIFICATION)
            .lastmodificationuser(UPDATED_LASTMODIFICATIONUSER);

        restODFMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedODF.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedODF))
            )
            .andExpect(status().isOk());

        // Validate the ODF in the database
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeUpdate);
        ODF testODF = oDFList.get(oDFList.size() - 1);
        assertThat(testODF.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testODF.getOperationName()).isEqualTo(UPDATED_OPERATION_NAME);
        assertThat(testODF.getDateofcreation()).isEqualTo(UPDATED_DATEOFCREATION);
        assertThat(testODF.getDateofmodification()).isEqualTo(UPDATED_DATEOFMODIFICATION);
        assertThat(testODF.getLastmodificationuser()).isEqualTo(UPDATED_LASTMODIFICATIONUSER);
    }

    @Test
    @Transactional
    void putNonExistingODF() throws Exception {
        int databaseSizeBeforeUpdate = oDFRepository.findAll().size();
        oDF.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restODFMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oDF.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oDF))
            )
            .andExpect(status().isBadRequest());

        // Validate the ODF in the database
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchODF() throws Exception {
        int databaseSizeBeforeUpdate = oDFRepository.findAll().size();
        oDF.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restODFMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oDF))
            )
            .andExpect(status().isBadRequest());

        // Validate the ODF in the database
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamODF() throws Exception {
        int databaseSizeBeforeUpdate = oDFRepository.findAll().size();
        oDF.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restODFMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oDF)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ODF in the database
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateODFWithPatch() throws Exception {
        // Initialize the database
        oDF.setId(UUID.randomUUID().toString());
        oDFRepository.saveAndFlush(oDF);

        int databaseSizeBeforeUpdate = oDFRepository.findAll().size();

        // Update the oDF using partial update
        ODF partialUpdatedODF = new ODF();
        partialUpdatedODF.setId(oDF.getId());

        partialUpdatedODF.status(UPDATED_STATUS).operationName(UPDATED_OPERATION_NAME).lastmodificationuser(UPDATED_LASTMODIFICATIONUSER);

        restODFMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedODF.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedODF))
            )
            .andExpect(status().isOk());

        // Validate the ODF in the database
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeUpdate);
        ODF testODF = oDFList.get(oDFList.size() - 1);
        assertThat(testODF.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testODF.getOperationName()).isEqualTo(UPDATED_OPERATION_NAME);
        assertThat(testODF.getDateofcreation()).isEqualTo(DEFAULT_DATEOFCREATION);
        assertThat(testODF.getDateofmodification()).isEqualTo(DEFAULT_DATEOFMODIFICATION);
        assertThat(testODF.getLastmodificationuser()).isEqualTo(UPDATED_LASTMODIFICATIONUSER);
    }

    @Test
    @Transactional
    void fullUpdateODFWithPatch() throws Exception {
        // Initialize the database
        oDF.setId(UUID.randomUUID().toString());
        oDFRepository.saveAndFlush(oDF);

        int databaseSizeBeforeUpdate = oDFRepository.findAll().size();

        // Update the oDF using partial update
        ODF partialUpdatedODF = new ODF();
        partialUpdatedODF.setId(oDF.getId());

        partialUpdatedODF
            .status(UPDATED_STATUS)
            .operationName(UPDATED_OPERATION_NAME)
            .dateofcreation(UPDATED_DATEOFCREATION)
            .dateofmodification(UPDATED_DATEOFMODIFICATION)
            .lastmodificationuser(UPDATED_LASTMODIFICATIONUSER);

        restODFMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedODF.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedODF))
            )
            .andExpect(status().isOk());

        // Validate the ODF in the database
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeUpdate);
        ODF testODF = oDFList.get(oDFList.size() - 1);
        assertThat(testODF.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testODF.getOperationName()).isEqualTo(UPDATED_OPERATION_NAME);
        assertThat(testODF.getDateofcreation()).isEqualTo(UPDATED_DATEOFCREATION);
        assertThat(testODF.getDateofmodification()).isEqualTo(UPDATED_DATEOFMODIFICATION);
        assertThat(testODF.getLastmodificationuser()).isEqualTo(UPDATED_LASTMODIFICATIONUSER);
    }

    @Test
    @Transactional
    void patchNonExistingODF() throws Exception {
        int databaseSizeBeforeUpdate = oDFRepository.findAll().size();
        oDF.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restODFMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oDF.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oDF))
            )
            .andExpect(status().isBadRequest());

        // Validate the ODF in the database
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchODF() throws Exception {
        int databaseSizeBeforeUpdate = oDFRepository.findAll().size();
        oDF.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restODFMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oDF))
            )
            .andExpect(status().isBadRequest());

        // Validate the ODF in the database
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamODF() throws Exception {
        int databaseSizeBeforeUpdate = oDFRepository.findAll().size();
        oDF.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restODFMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(oDF)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ODF in the database
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteODF() throws Exception {
        // Initialize the database
        oDF.setId(UUID.randomUUID().toString());
        oDFRepository.saveAndFlush(oDF);

        int databaseSizeBeforeDelete = oDFRepository.findAll().size();

        // Delete the oDF
        restODFMockMvc.perform(delete(ENTITY_API_URL_ID, oDF.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ODF> oDFList = oDFRepository.findAll();
        assertThat(oDFList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
