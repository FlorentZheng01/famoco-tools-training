package com.famoco.myfirstjhipster.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.famoco.myfirstjhipster.IntegrationTest;
import com.famoco.myfirstjhipster.domain.FileName;
import com.famoco.myfirstjhipster.repository.FileNameRepository;
import com.famoco.myfirstjhipster.service.dto.FileNameDTO;
import com.famoco.myfirstjhipster.service.mapper.FileNameMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FileNameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileNameResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/file-names";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileNameRepository fileNameRepository;

    @Autowired
    private FileNameMapper fileNameMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileNameMockMvc;

    private FileName fileName;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileName createEntity(EntityManager em) {
        FileName fileName = new FileName()
            .name(DEFAULT_NAME)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return fileName;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileName createUpdatedEntity(EntityManager em) {
        FileName fileName = new FileName()
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        return fileName;
    }

    @BeforeEach
    public void initTest() {
        fileName = createEntity(em);
    }

    @Test
    @Transactional
    void createFileName() throws Exception {
        int databaseSizeBeforeCreate = fileNameRepository.findAll().size();
        // Create the FileName
        FileNameDTO fileNameDTO = fileNameMapper.toDto(fileName);
        restFileNameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileNameDTO)))
            .andExpect(status().isCreated());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeCreate + 1);
        FileName testFileName = fileNameList.get(fileNameList.size() - 1);
        assertThat(testFileName.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFileName.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testFileName.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testFileName.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFileNameWithExistingId() throws Exception {
        // Create the FileName with an existing ID
        fileName.setId(1L);
        FileNameDTO fileNameDTO = fileNameMapper.toDto(fileName);

        int databaseSizeBeforeCreate = fileNameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileNameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileNameDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFileNames() throws Exception {
        // Initialize the database
        fileNameRepository.saveAndFlush(fileName);

        // Get all the fileNameList
        restFileNameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileName.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFileName() throws Exception {
        // Initialize the database
        fileNameRepository.saveAndFlush(fileName);

        // Get the fileName
        restFileNameMockMvc
            .perform(get(ENTITY_API_URL_ID, fileName.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileName.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingFileName() throws Exception {
        // Get the fileName
        restFileNameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFileName() throws Exception {
        // Initialize the database
        fileNameRepository.saveAndFlush(fileName);

        int databaseSizeBeforeUpdate = fileNameRepository.findAll().size();

        // Update the fileName
        FileName updatedFileName = fileNameRepository.findById(fileName.getId()).get();
        // Disconnect from session so that the updates on updatedFileName are not directly saved in db
        em.detach(updatedFileName);
        updatedFileName
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        FileNameDTO fileNameDTO = fileNameMapper.toDto(updatedFileName);

        restFileNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileNameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileNameDTO))
            )
            .andExpect(status().isOk());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeUpdate);
        FileName testFileName = fileNameList.get(fileNameList.size() - 1);
        assertThat(testFileName.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFileName.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testFileName.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testFileName.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFileName() throws Exception {
        int databaseSizeBeforeUpdate = fileNameRepository.findAll().size();
        fileName.setId(count.incrementAndGet());

        // Create the FileName
        FileNameDTO fileNameDTO = fileNameMapper.toDto(fileName);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileNameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileNameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileName() throws Exception {
        int databaseSizeBeforeUpdate = fileNameRepository.findAll().size();
        fileName.setId(count.incrementAndGet());

        // Create the FileName
        FileNameDTO fileNameDTO = fileNameMapper.toDto(fileName);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileNameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileName() throws Exception {
        int databaseSizeBeforeUpdate = fileNameRepository.findAll().size();
        fileName.setId(count.incrementAndGet());

        // Create the FileName
        FileNameDTO fileNameDTO = fileNameMapper.toDto(fileName);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileNameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileNameDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileNameWithPatch() throws Exception {
        // Initialize the database
        fileNameRepository.saveAndFlush(fileName);

        int databaseSizeBeforeUpdate = fileNameRepository.findAll().size();

        // Update the fileName using partial update
        FileName partialUpdatedFileName = new FileName();
        partialUpdatedFileName.setId(fileName.getId());

        partialUpdatedFileName.content(UPDATED_CONTENT).contentContentType(UPDATED_CONTENT_CONTENT_TYPE).description(UPDATED_DESCRIPTION);

        restFileNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileName))
            )
            .andExpect(status().isOk());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeUpdate);
        FileName testFileName = fileNameList.get(fileNameList.size() - 1);
        assertThat(testFileName.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFileName.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testFileName.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testFileName.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFileNameWithPatch() throws Exception {
        // Initialize the database
        fileNameRepository.saveAndFlush(fileName);

        int databaseSizeBeforeUpdate = fileNameRepository.findAll().size();

        // Update the fileName using partial update
        FileName partialUpdatedFileName = new FileName();
        partialUpdatedFileName.setId(fileName.getId());

        partialUpdatedFileName
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);

        restFileNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileName))
            )
            .andExpect(status().isOk());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeUpdate);
        FileName testFileName = fileNameList.get(fileNameList.size() - 1);
        assertThat(testFileName.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFileName.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testFileName.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testFileName.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFileName() throws Exception {
        int databaseSizeBeforeUpdate = fileNameRepository.findAll().size();
        fileName.setId(count.incrementAndGet());

        // Create the FileName
        FileNameDTO fileNameDTO = fileNameMapper.toDto(fileName);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileNameDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileNameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileName() throws Exception {
        int databaseSizeBeforeUpdate = fileNameRepository.findAll().size();
        fileName.setId(count.incrementAndGet());

        // Create the FileName
        FileNameDTO fileNameDTO = fileNameMapper.toDto(fileName);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileNameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileName() throws Exception {
        int databaseSizeBeforeUpdate = fileNameRepository.findAll().size();
        fileName.setId(count.incrementAndGet());

        // Create the FileName
        FileNameDTO fileNameDTO = fileNameMapper.toDto(fileName);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileNameMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fileNameDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileName in the database
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFileName() throws Exception {
        // Initialize the database
        fileNameRepository.saveAndFlush(fileName);

        int databaseSizeBeforeDelete = fileNameRepository.findAll().size();

        // Delete the fileName
        restFileNameMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileName.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileName> fileNameList = fileNameRepository.findAll();
        assertThat(fileNameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
