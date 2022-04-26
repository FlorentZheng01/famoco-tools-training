package com.famoco.myfirstjhipster.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.famoco.myfirstjhipster.IntegrationTest;
import com.famoco.myfirstjhipster.domain.Retailer;
import com.famoco.myfirstjhipster.repository.RetailerRepository;
import com.famoco.myfirstjhipster.service.criteria.RetailerCriteria;
import com.famoco.myfirstjhipster.service.dto.RetailerDTO;
import com.famoco.myfirstjhipster.service.mapper.RetailerMapper;
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
 * Integration tests for the {@link RetailerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RetailerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE_NUMBER = 1;
    private static final Integer UPDATED_PHONE_NUMBER = 2;
    private static final Integer SMALLER_PHONE_NUMBER = 1 - 1;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/retailers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private RetailerMapper retailerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRetailerMockMvc;

    private Retailer retailer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retailer createEntity(EntityManager em) {
        Retailer retailer = new Retailer().name(DEFAULT_NAME).phoneNumber(DEFAULT_PHONE_NUMBER).address(DEFAULT_ADDRESS).mail(DEFAULT_MAIL);
        return retailer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retailer createUpdatedEntity(EntityManager em) {
        Retailer retailer = new Retailer().name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).address(UPDATED_ADDRESS).mail(UPDATED_MAIL);
        return retailer;
    }

    @BeforeEach
    public void initTest() {
        retailer = createEntity(em);
    }

    @Test
    @Transactional
    void createRetailer() throws Exception {
        int databaseSizeBeforeCreate = retailerRepository.findAll().size();
        // Create the Retailer
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);
        restRetailerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailerDTO)))
            .andExpect(status().isCreated());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeCreate + 1);
        Retailer testRetailer = retailerList.get(retailerList.size() - 1);
        assertThat(testRetailer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRetailer.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testRetailer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testRetailer.getMail()).isEqualTo(DEFAULT_MAIL);
    }

    @Test
    @Transactional
    void createRetailerWithExistingId() throws Exception {
        // Create the Retailer with an existing ID
        retailer.setId(1L);
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);

        int databaseSizeBeforeCreate = retailerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRetailerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = retailerRepository.findAll().size();
        // set the field null
        retailer.setName(null);

        // Create the Retailer, which fails.
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);

        restRetailerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailerDTO)))
            .andExpect(status().isBadRequest());

        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = retailerRepository.findAll().size();
        // set the field null
        retailer.setPhoneNumber(null);

        // Create the Retailer, which fails.
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);

        restRetailerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailerDTO)))
            .andExpect(status().isBadRequest());

        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = retailerRepository.findAll().size();
        // set the field null
        retailer.setAddress(null);

        // Create the Retailer, which fails.
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);

        restRetailerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailerDTO)))
            .andExpect(status().isBadRequest());

        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMailIsRequired() throws Exception {
        int databaseSizeBeforeTest = retailerRepository.findAll().size();
        // set the field null
        retailer.setMail(null);

        // Create the Retailer, which fails.
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);

        restRetailerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailerDTO)))
            .andExpect(status().isBadRequest());

        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRetailers() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList
        restRetailerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(retailer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)));
    }

    @Test
    @Transactional
    void getRetailer() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get the retailer
        restRetailerMockMvc
            .perform(get(ENTITY_API_URL_ID, retailer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(retailer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL));
    }

    @Test
    @Transactional
    void getRetailersByIdFiltering() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        Long id = retailer.getId();

        defaultRetailerShouldBeFound("id.equals=" + id);
        defaultRetailerShouldNotBeFound("id.notEquals=" + id);

        defaultRetailerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRetailerShouldNotBeFound("id.greaterThan=" + id);

        defaultRetailerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRetailerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRetailersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where name equals to DEFAULT_NAME
        defaultRetailerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the retailerList where name equals to UPDATED_NAME
        defaultRetailerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRetailersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where name not equals to DEFAULT_NAME
        defaultRetailerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the retailerList where name not equals to UPDATED_NAME
        defaultRetailerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRetailersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRetailerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the retailerList where name equals to UPDATED_NAME
        defaultRetailerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRetailersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where name is not null
        defaultRetailerShouldBeFound("name.specified=true");

        // Get all the retailerList where name is null
        defaultRetailerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRetailersByNameContainsSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where name contains DEFAULT_NAME
        defaultRetailerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the retailerList where name contains UPDATED_NAME
        defaultRetailerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRetailersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where name does not contain DEFAULT_NAME
        defaultRetailerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the retailerList where name does not contain UPDATED_NAME
        defaultRetailerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRetailersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultRetailerShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the retailerList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultRetailerShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRetailersByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultRetailerShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the retailerList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultRetailerShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRetailersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultRetailerShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the retailerList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultRetailerShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRetailersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where phoneNumber is not null
        defaultRetailerShouldBeFound("phoneNumber.specified=true");

        // Get all the retailerList where phoneNumber is null
        defaultRetailerShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRetailersByPhoneNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where phoneNumber is greater than or equal to DEFAULT_PHONE_NUMBER
        defaultRetailerShouldBeFound("phoneNumber.greaterThanOrEqual=" + DEFAULT_PHONE_NUMBER);

        // Get all the retailerList where phoneNumber is greater than or equal to UPDATED_PHONE_NUMBER
        defaultRetailerShouldNotBeFound("phoneNumber.greaterThanOrEqual=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRetailersByPhoneNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where phoneNumber is less than or equal to DEFAULT_PHONE_NUMBER
        defaultRetailerShouldBeFound("phoneNumber.lessThanOrEqual=" + DEFAULT_PHONE_NUMBER);

        // Get all the retailerList where phoneNumber is less than or equal to SMALLER_PHONE_NUMBER
        defaultRetailerShouldNotBeFound("phoneNumber.lessThanOrEqual=" + SMALLER_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRetailersByPhoneNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where phoneNumber is less than DEFAULT_PHONE_NUMBER
        defaultRetailerShouldNotBeFound("phoneNumber.lessThan=" + DEFAULT_PHONE_NUMBER);

        // Get all the retailerList where phoneNumber is less than UPDATED_PHONE_NUMBER
        defaultRetailerShouldBeFound("phoneNumber.lessThan=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRetailersByPhoneNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where phoneNumber is greater than DEFAULT_PHONE_NUMBER
        defaultRetailerShouldNotBeFound("phoneNumber.greaterThan=" + DEFAULT_PHONE_NUMBER);

        // Get all the retailerList where phoneNumber is greater than SMALLER_PHONE_NUMBER
        defaultRetailerShouldBeFound("phoneNumber.greaterThan=" + SMALLER_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRetailersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where address equals to DEFAULT_ADDRESS
        defaultRetailerShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the retailerList where address equals to UPDATED_ADDRESS
        defaultRetailerShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllRetailersByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where address not equals to DEFAULT_ADDRESS
        defaultRetailerShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the retailerList where address not equals to UPDATED_ADDRESS
        defaultRetailerShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllRetailersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultRetailerShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the retailerList where address equals to UPDATED_ADDRESS
        defaultRetailerShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllRetailersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where address is not null
        defaultRetailerShouldBeFound("address.specified=true");

        // Get all the retailerList where address is null
        defaultRetailerShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllRetailersByAddressContainsSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where address contains DEFAULT_ADDRESS
        defaultRetailerShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the retailerList where address contains UPDATED_ADDRESS
        defaultRetailerShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllRetailersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where address does not contain DEFAULT_ADDRESS
        defaultRetailerShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the retailerList where address does not contain UPDATED_ADDRESS
        defaultRetailerShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllRetailersByMailIsEqualToSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where mail equals to DEFAULT_MAIL
        defaultRetailerShouldBeFound("mail.equals=" + DEFAULT_MAIL);

        // Get all the retailerList where mail equals to UPDATED_MAIL
        defaultRetailerShouldNotBeFound("mail.equals=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllRetailersByMailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where mail not equals to DEFAULT_MAIL
        defaultRetailerShouldNotBeFound("mail.notEquals=" + DEFAULT_MAIL);

        // Get all the retailerList where mail not equals to UPDATED_MAIL
        defaultRetailerShouldBeFound("mail.notEquals=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllRetailersByMailIsInShouldWork() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where mail in DEFAULT_MAIL or UPDATED_MAIL
        defaultRetailerShouldBeFound("mail.in=" + DEFAULT_MAIL + "," + UPDATED_MAIL);

        // Get all the retailerList where mail equals to UPDATED_MAIL
        defaultRetailerShouldNotBeFound("mail.in=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllRetailersByMailIsNullOrNotNull() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where mail is not null
        defaultRetailerShouldBeFound("mail.specified=true");

        // Get all the retailerList where mail is null
        defaultRetailerShouldNotBeFound("mail.specified=false");
    }

    @Test
    @Transactional
    void getAllRetailersByMailContainsSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where mail contains DEFAULT_MAIL
        defaultRetailerShouldBeFound("mail.contains=" + DEFAULT_MAIL);

        // Get all the retailerList where mail contains UPDATED_MAIL
        defaultRetailerShouldNotBeFound("mail.contains=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllRetailersByMailNotContainsSomething() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList where mail does not contain DEFAULT_MAIL
        defaultRetailerShouldNotBeFound("mail.doesNotContain=" + DEFAULT_MAIL);

        // Get all the retailerList where mail does not contain UPDATED_MAIL
        defaultRetailerShouldBeFound("mail.doesNotContain=" + UPDATED_MAIL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRetailerShouldBeFound(String filter) throws Exception {
        restRetailerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(retailer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)));

        // Check, that the count call also returns 1
        restRetailerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRetailerShouldNotBeFound(String filter) throws Exception {
        restRetailerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRetailerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRetailer() throws Exception {
        // Get the retailer
        restRetailerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRetailer() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();

        // Update the retailer
        Retailer updatedRetailer = retailerRepository.findById(retailer.getId()).get();
        // Disconnect from session so that the updates on updatedRetailer are not directly saved in db
        em.detach(updatedRetailer);
        updatedRetailer.name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).address(UPDATED_ADDRESS).mail(UPDATED_MAIL);
        RetailerDTO retailerDTO = retailerMapper.toDto(updatedRetailer);

        restRetailerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, retailerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retailerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
        Retailer testRetailer = retailerList.get(retailerList.size() - 1);
        assertThat(testRetailer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRetailer.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testRetailer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testRetailer.getMail()).isEqualTo(UPDATED_MAIL);
    }

    @Test
    @Transactional
    void putNonExistingRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setId(count.incrementAndGet());

        // Create the Retailer
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, retailerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retailerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setId(count.incrementAndGet());

        // Create the Retailer
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retailerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setId(count.incrementAndGet());

        // Create the Retailer
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRetailerWithPatch() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();

        // Update the retailer using partial update
        Retailer partialUpdatedRetailer = new Retailer();
        partialUpdatedRetailer.setId(retailer.getId());

        partialUpdatedRetailer.mail(UPDATED_MAIL);

        restRetailerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetailer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetailer))
            )
            .andExpect(status().isOk());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
        Retailer testRetailer = retailerList.get(retailerList.size() - 1);
        assertThat(testRetailer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRetailer.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testRetailer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testRetailer.getMail()).isEqualTo(UPDATED_MAIL);
    }

    @Test
    @Transactional
    void fullUpdateRetailerWithPatch() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();

        // Update the retailer using partial update
        Retailer partialUpdatedRetailer = new Retailer();
        partialUpdatedRetailer.setId(retailer.getId());

        partialUpdatedRetailer.name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).address(UPDATED_ADDRESS).mail(UPDATED_MAIL);

        restRetailerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetailer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetailer))
            )
            .andExpect(status().isOk());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
        Retailer testRetailer = retailerList.get(retailerList.size() - 1);
        assertThat(testRetailer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRetailer.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testRetailer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testRetailer.getMail()).isEqualTo(UPDATED_MAIL);
    }

    @Test
    @Transactional
    void patchNonExistingRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setId(count.incrementAndGet());

        // Create the Retailer
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, retailerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retailerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setId(count.incrementAndGet());

        // Create the Retailer
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retailerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setId(count.incrementAndGet());

        // Create the Retailer
        RetailerDTO retailerDTO = retailerMapper.toDto(retailer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(retailerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRetailer() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        int databaseSizeBeforeDelete = retailerRepository.findAll().size();

        // Delete the retailer
        restRetailerMockMvc
            .perform(delete(ENTITY_API_URL_ID, retailer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
