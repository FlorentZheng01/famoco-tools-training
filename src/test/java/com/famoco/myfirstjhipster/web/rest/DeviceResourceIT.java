package com.famoco.myfirstjhipster.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.famoco.myfirstjhipster.IntegrationTest;
import com.famoco.myfirstjhipster.domain.Device;
import com.famoco.myfirstjhipster.domain.Retailer;
import com.famoco.myfirstjhipster.repository.DeviceRepository;
import com.famoco.myfirstjhipster.service.criteria.DeviceCriteria;
import com.famoco.myfirstjhipster.service.dto.DeviceDTO;
import com.famoco.myfirstjhipster.service.mapper.DeviceMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link DeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceResourceIT {

    private static final Integer DEFAULT_FAMOCO_ID = 1;
    private static final Integer UPDATED_FAMOCO_ID = 2;
    private static final Integer SMALLER_FAMOCO_ID = 1 - 1;

    private static final String DEFAULT_MAC_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_MAC_ADDRESS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CREATION = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceMockMvc;

    private Device device;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity(EntityManager em) {
        Device device = new Device().famocoId(DEFAULT_FAMOCO_ID).macAddress(DEFAULT_MAC_ADDRESS).dateCreation(DEFAULT_DATE_CREATION);
        // Add required entity
        Retailer retailer;
        if (TestUtil.findAll(em, Retailer.class).isEmpty()) {
            retailer = RetailerResourceIT.createEntity(em);
            em.persist(retailer);
            em.flush();
        } else {
            retailer = TestUtil.findAll(em, Retailer.class).get(0);
        }
        device.setRetailer(retailer);
        return device;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createUpdatedEntity(EntityManager em) {
        Device device = new Device().famocoId(UPDATED_FAMOCO_ID).macAddress(UPDATED_MAC_ADDRESS).dateCreation(UPDATED_DATE_CREATION);
        // Add required entity
        Retailer retailer;
        if (TestUtil.findAll(em, Retailer.class).isEmpty()) {
            retailer = RetailerResourceIT.createUpdatedEntity(em);
            em.persist(retailer);
            em.flush();
        } else {
            retailer = TestUtil.findAll(em, Retailer.class).get(0);
        }
        device.setRetailer(retailer);
        return device;
    }

    @BeforeEach
    public void initTest() {
        device = createEntity(em);
    }

    @Test
    @Transactional
    void createDevice() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();
        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isCreated());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate + 1);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getFamocoId()).isEqualTo(DEFAULT_FAMOCO_ID);
        assertThat(testDevice.getMacAddress()).isEqualTo(DEFAULT_MAC_ADDRESS);
        assertThat(testDevice.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void createDeviceWithExistingId() throws Exception {
        // Create the Device with an existing ID
        device.setId(1L);
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFamocoIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setFamocoId(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMacAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setMacAddress(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setDateCreation(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDevices() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].famocoId").value(hasItem(DEFAULT_FAMOCO_ID)))
            .andExpect(jsonPath("$.[*].macAddress").value(hasItem(DEFAULT_MAC_ADDRESS)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));
    }

    @Test
    @Transactional
    void getDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().intValue()))
            .andExpect(jsonPath("$.famocoId").value(DEFAULT_FAMOCO_ID))
            .andExpect(jsonPath("$.macAddress").value(DEFAULT_MAC_ADDRESS))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()));
    }

    @Test
    @Transactional
    void getDevicesByIdFiltering() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        Long id = device.getId();

        defaultDeviceShouldBeFound("id.equals=" + id);
        defaultDeviceShouldNotBeFound("id.notEquals=" + id);

        defaultDeviceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeviceShouldNotBeFound("id.greaterThan=" + id);

        defaultDeviceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeviceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDevicesByFamocoIdIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where famocoId equals to DEFAULT_FAMOCO_ID
        defaultDeviceShouldBeFound("famocoId.equals=" + DEFAULT_FAMOCO_ID);

        // Get all the deviceList where famocoId equals to UPDATED_FAMOCO_ID
        defaultDeviceShouldNotBeFound("famocoId.equals=" + UPDATED_FAMOCO_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByFamocoIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where famocoId not equals to DEFAULT_FAMOCO_ID
        defaultDeviceShouldNotBeFound("famocoId.notEquals=" + DEFAULT_FAMOCO_ID);

        // Get all the deviceList where famocoId not equals to UPDATED_FAMOCO_ID
        defaultDeviceShouldBeFound("famocoId.notEquals=" + UPDATED_FAMOCO_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByFamocoIdIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where famocoId in DEFAULT_FAMOCO_ID or UPDATED_FAMOCO_ID
        defaultDeviceShouldBeFound("famocoId.in=" + DEFAULT_FAMOCO_ID + "," + UPDATED_FAMOCO_ID);

        // Get all the deviceList where famocoId equals to UPDATED_FAMOCO_ID
        defaultDeviceShouldNotBeFound("famocoId.in=" + UPDATED_FAMOCO_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByFamocoIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where famocoId is not null
        defaultDeviceShouldBeFound("famocoId.specified=true");

        // Get all the deviceList where famocoId is null
        defaultDeviceShouldNotBeFound("famocoId.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByFamocoIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where famocoId is greater than or equal to DEFAULT_FAMOCO_ID
        defaultDeviceShouldBeFound("famocoId.greaterThanOrEqual=" + DEFAULT_FAMOCO_ID);

        // Get all the deviceList where famocoId is greater than or equal to UPDATED_FAMOCO_ID
        defaultDeviceShouldNotBeFound("famocoId.greaterThanOrEqual=" + UPDATED_FAMOCO_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByFamocoIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where famocoId is less than or equal to DEFAULT_FAMOCO_ID
        defaultDeviceShouldBeFound("famocoId.lessThanOrEqual=" + DEFAULT_FAMOCO_ID);

        // Get all the deviceList where famocoId is less than or equal to SMALLER_FAMOCO_ID
        defaultDeviceShouldNotBeFound("famocoId.lessThanOrEqual=" + SMALLER_FAMOCO_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByFamocoIdIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where famocoId is less than DEFAULT_FAMOCO_ID
        defaultDeviceShouldNotBeFound("famocoId.lessThan=" + DEFAULT_FAMOCO_ID);

        // Get all the deviceList where famocoId is less than UPDATED_FAMOCO_ID
        defaultDeviceShouldBeFound("famocoId.lessThan=" + UPDATED_FAMOCO_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByFamocoIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where famocoId is greater than DEFAULT_FAMOCO_ID
        defaultDeviceShouldNotBeFound("famocoId.greaterThan=" + DEFAULT_FAMOCO_ID);

        // Get all the deviceList where famocoId is greater than SMALLER_FAMOCO_ID
        defaultDeviceShouldBeFound("famocoId.greaterThan=" + SMALLER_FAMOCO_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByMacAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where macAddress equals to DEFAULT_MAC_ADDRESS
        defaultDeviceShouldBeFound("macAddress.equals=" + DEFAULT_MAC_ADDRESS);

        // Get all the deviceList where macAddress equals to UPDATED_MAC_ADDRESS
        defaultDeviceShouldNotBeFound("macAddress.equals=" + UPDATED_MAC_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDevicesByMacAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where macAddress not equals to DEFAULT_MAC_ADDRESS
        defaultDeviceShouldNotBeFound("macAddress.notEquals=" + DEFAULT_MAC_ADDRESS);

        // Get all the deviceList where macAddress not equals to UPDATED_MAC_ADDRESS
        defaultDeviceShouldBeFound("macAddress.notEquals=" + UPDATED_MAC_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDevicesByMacAddressIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where macAddress in DEFAULT_MAC_ADDRESS or UPDATED_MAC_ADDRESS
        defaultDeviceShouldBeFound("macAddress.in=" + DEFAULT_MAC_ADDRESS + "," + UPDATED_MAC_ADDRESS);

        // Get all the deviceList where macAddress equals to UPDATED_MAC_ADDRESS
        defaultDeviceShouldNotBeFound("macAddress.in=" + UPDATED_MAC_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDevicesByMacAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where macAddress is not null
        defaultDeviceShouldBeFound("macAddress.specified=true");

        // Get all the deviceList where macAddress is null
        defaultDeviceShouldNotBeFound("macAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByMacAddressContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where macAddress contains DEFAULT_MAC_ADDRESS
        defaultDeviceShouldBeFound("macAddress.contains=" + DEFAULT_MAC_ADDRESS);

        // Get all the deviceList where macAddress contains UPDATED_MAC_ADDRESS
        defaultDeviceShouldNotBeFound("macAddress.contains=" + UPDATED_MAC_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDevicesByMacAddressNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where macAddress does not contain DEFAULT_MAC_ADDRESS
        defaultDeviceShouldNotBeFound("macAddress.doesNotContain=" + DEFAULT_MAC_ADDRESS);

        // Get all the deviceList where macAddress does not contain UPDATED_MAC_ADDRESS
        defaultDeviceShouldBeFound("macAddress.doesNotContain=" + UPDATED_MAC_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDevicesByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultDeviceShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the deviceList where dateCreation equals to UPDATED_DATE_CREATION
        defaultDeviceShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllDevicesByDateCreationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where dateCreation not equals to DEFAULT_DATE_CREATION
        defaultDeviceShouldNotBeFound("dateCreation.notEquals=" + DEFAULT_DATE_CREATION);

        // Get all the deviceList where dateCreation not equals to UPDATED_DATE_CREATION
        defaultDeviceShouldBeFound("dateCreation.notEquals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllDevicesByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultDeviceShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the deviceList where dateCreation equals to UPDATED_DATE_CREATION
        defaultDeviceShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllDevicesByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where dateCreation is not null
        defaultDeviceShouldBeFound("dateCreation.specified=true");

        // Get all the deviceList where dateCreation is null
        defaultDeviceShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByDateCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where dateCreation is greater than or equal to DEFAULT_DATE_CREATION
        defaultDeviceShouldBeFound("dateCreation.greaterThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the deviceList where dateCreation is greater than or equal to UPDATED_DATE_CREATION
        defaultDeviceShouldNotBeFound("dateCreation.greaterThanOrEqual=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllDevicesByDateCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where dateCreation is less than or equal to DEFAULT_DATE_CREATION
        defaultDeviceShouldBeFound("dateCreation.lessThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the deviceList where dateCreation is less than or equal to SMALLER_DATE_CREATION
        defaultDeviceShouldNotBeFound("dateCreation.lessThanOrEqual=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllDevicesByDateCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where dateCreation is less than DEFAULT_DATE_CREATION
        defaultDeviceShouldNotBeFound("dateCreation.lessThan=" + DEFAULT_DATE_CREATION);

        // Get all the deviceList where dateCreation is less than UPDATED_DATE_CREATION
        defaultDeviceShouldBeFound("dateCreation.lessThan=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllDevicesByDateCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where dateCreation is greater than DEFAULT_DATE_CREATION
        defaultDeviceShouldNotBeFound("dateCreation.greaterThan=" + DEFAULT_DATE_CREATION);

        // Get all the deviceList where dateCreation is greater than SMALLER_DATE_CREATION
        defaultDeviceShouldBeFound("dateCreation.greaterThan=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllDevicesByRetailerIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);
        Retailer retailer;
        if (TestUtil.findAll(em, Retailer.class).isEmpty()) {
            retailer = RetailerResourceIT.createEntity(em);
            em.persist(retailer);
            em.flush();
        } else {
            retailer = TestUtil.findAll(em, Retailer.class).get(0);
        }
        em.persist(retailer);
        em.flush();
        device.setRetailer(retailer);
        deviceRepository.saveAndFlush(device);
        Long retailerId = retailer.getId();

        // Get all the deviceList where retailer equals to retailerId
        defaultDeviceShouldBeFound("retailerId.equals=" + retailerId);

        // Get all the deviceList where retailer equals to (retailerId + 1)
        defaultDeviceShouldNotBeFound("retailerId.equals=" + (retailerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeviceShouldBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].famocoId").value(hasItem(DEFAULT_FAMOCO_ID)))
            .andExpect(jsonPath("$.[*].macAddress").value(hasItem(DEFAULT_MAC_ADDRESS)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));

        // Check, that the count call also returns 1
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeviceShouldNotBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).get();
        // Disconnect from session so that the updates on updatedDevice are not directly saved in db
        em.detach(updatedDevice);
        updatedDevice.famocoId(UPDATED_FAMOCO_ID).macAddress(UPDATED_MAC_ADDRESS).dateCreation(UPDATED_DATE_CREATION);
        DeviceDTO deviceDTO = deviceMapper.toDto(updatedDevice);

        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getFamocoId()).isEqualTo(UPDATED_FAMOCO_ID);
        assertThat(testDevice.getMacAddress()).isEqualTo(UPDATED_MAC_ADDRESS);
        assertThat(testDevice.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void putNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getFamocoId()).isEqualTo(DEFAULT_FAMOCO_ID);
        assertThat(testDevice.getMacAddress()).isEqualTo(DEFAULT_MAC_ADDRESS);
        assertThat(testDevice.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void fullUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice.famocoId(UPDATED_FAMOCO_ID).macAddress(UPDATED_MAC_ADDRESS).dateCreation(UPDATED_DATE_CREATION);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getFamocoId()).isEqualTo(UPDATED_FAMOCO_ID);
        assertThat(testDevice.getMacAddress()).isEqualTo(UPDATED_MAC_ADDRESS);
        assertThat(testDevice.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void patchNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeDelete = deviceRepository.findAll().size();

        // Delete the device
        restDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, device.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
