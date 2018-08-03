package com.prolabs.web.rest;

import com.prolabs.SlackbotsApp;

import com.prolabs.domain.ConfigData;
import com.prolabs.repository.ConfigDataRepository;
import com.prolabs.service.ConfigDataService;
import com.prolabs.repository.search.ConfigDataSearchRepository;
import com.prolabs.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.prolabs.web.rest.TestUtil.sameInstant;
import static com.prolabs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prolabs.domain.enumeration.YesNoFlag;
/**
 * Test class for the ConfigDataResource REST controller.
 *
 * @see ConfigDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackbotsApp.class)
public class ConfigDataResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final YesNoFlag DEFAULT_ENCRYPTED = YesNoFlag.Y;
    private static final YesNoFlag UPDATED_ENCRYPTED = YesNoFlag.N;

    private static final ZonedDateTime DEFAULT_CREATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_UPDATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private ConfigDataRepository configDataRepository;

    @Autowired
    private ConfigDataService configDataService;

    @Autowired
    private ConfigDataSearchRepository configDataSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConfigDataMockMvc;

    private ConfigData configData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigDataResource configDataResource = new ConfigDataResource(configDataService);
        this.restConfigDataMockMvc = MockMvcBuilders.standaloneSetup(configDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigData createEntity(EntityManager em) {
        ConfigData configData = new ConfigData()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .encrypted(DEFAULT_ENCRYPTED)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY);
        return configData;
    }

    @Before
    public void initTest() {
        configDataSearchRepository.deleteAll();
        configData = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigData() throws Exception {
        int databaseSizeBeforeCreate = configDataRepository.findAll().size();

        // Create the ConfigData
        restConfigDataMockMvc.perform(post("/api/config-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configData)))
            .andExpect(status().isCreated());

        // Validate the ConfigData in the database
        List<ConfigData> configDataList = configDataRepository.findAll();
        assertThat(configDataList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigData testConfigData = configDataList.get(configDataList.size() - 1);
        assertThat(testConfigData.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testConfigData.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testConfigData.getEncrypted()).isEqualTo(DEFAULT_ENCRYPTED);
        assertThat(testConfigData.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testConfigData.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testConfigData.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testConfigData.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

        // Validate the ConfigData in Elasticsearch
        ConfigData configDataEs = configDataSearchRepository.findOne(testConfigData.getId());
        assertThat(configDataEs).isEqualToComparingFieldByField(testConfigData);
    }

    @Test
    @Transactional
    public void createConfigDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configDataRepository.findAll().size();

        // Create the ConfigData with an existing ID
        configData.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigDataMockMvc.perform(post("/api/config-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configData)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigData in the database
        List<ConfigData> configDataList = configDataRepository.findAll();
        assertThat(configDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = configDataRepository.findAll().size();
        // set the field null
        configData.setKey(null);

        // Create the ConfigData, which fails.

        restConfigDataMockMvc.perform(post("/api/config-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configData)))
            .andExpect(status().isBadRequest());

        List<ConfigData> configDataList = configDataRepository.findAll();
        assertThat(configDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = configDataRepository.findAll().size();
        // set the field null
        configData.setValue(null);

        // Create the ConfigData, which fails.

        restConfigDataMockMvc.perform(post("/api/config-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configData)))
            .andExpect(status().isBadRequest());

        List<ConfigData> configDataList = configDataRepository.findAll();
        assertThat(configDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEncryptedIsRequired() throws Exception {
        int databaseSizeBeforeTest = configDataRepository.findAll().size();
        // set the field null
        configData.setEncrypted(null);

        // Create the ConfigData, which fails.

        restConfigDataMockMvc.perform(post("/api/config-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configData)))
            .andExpect(status().isBadRequest());

        List<ConfigData> configDataList = configDataRepository.findAll();
        assertThat(configDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfigData() throws Exception {
        // Initialize the database
        configDataRepository.saveAndFlush(configData);

        // Get all the configDataList
        restConfigDataMockMvc.perform(get("/api/config-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configData.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].encrypted").value(hasItem(DEFAULT_ENCRYPTED.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(sameInstant(DEFAULT_CREATED_ON))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(sameInstant(DEFAULT_UPDATED_ON))))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getConfigData() throws Exception {
        // Initialize the database
        configDataRepository.saveAndFlush(configData);

        // Get the configData
        restConfigDataMockMvc.perform(get("/api/config-data/{id}", configData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configData.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.encrypted").value(DEFAULT_ENCRYPTED.toString()))
            .andExpect(jsonPath("$.createdOn").value(sameInstant(DEFAULT_CREATED_ON)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.updatedOn").value(sameInstant(DEFAULT_UPDATED_ON)))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfigData() throws Exception {
        // Get the configData
        restConfigDataMockMvc.perform(get("/api/config-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigData() throws Exception {
        // Initialize the database
        configDataService.save(configData);

        int databaseSizeBeforeUpdate = configDataRepository.findAll().size();

        // Update the configData
        ConfigData updatedConfigData = configDataRepository.findOne(configData.getId());
        // Disconnect from session so that the updates on updatedConfigData are not directly saved in db
        em.detach(updatedConfigData);
        updatedConfigData
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .encrypted(UPDATED_ENCRYPTED)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY);

        restConfigDataMockMvc.perform(put("/api/config-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigData)))
            .andExpect(status().isOk());

        // Validate the ConfigData in the database
        List<ConfigData> configDataList = configDataRepository.findAll();
        assertThat(configDataList).hasSize(databaseSizeBeforeUpdate);
        ConfigData testConfigData = configDataList.get(configDataList.size() - 1);
        assertThat(testConfigData.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConfigData.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testConfigData.getEncrypted()).isEqualTo(UPDATED_ENCRYPTED);
        assertThat(testConfigData.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testConfigData.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testConfigData.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testConfigData.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

        // Validate the ConfigData in Elasticsearch
        ConfigData configDataEs = configDataSearchRepository.findOne(testConfigData.getId());
        assertThat(configDataEs).isEqualToComparingFieldByField(testConfigData);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigData() throws Exception {
        int databaseSizeBeforeUpdate = configDataRepository.findAll().size();

        // Create the ConfigData

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConfigDataMockMvc.perform(put("/api/config-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configData)))
            .andExpect(status().isCreated());

        // Validate the ConfigData in the database
        List<ConfigData> configDataList = configDataRepository.findAll();
        assertThat(configDataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConfigData() throws Exception {
        // Initialize the database
        configDataService.save(configData);

        int databaseSizeBeforeDelete = configDataRepository.findAll().size();

        // Get the configData
        restConfigDataMockMvc.perform(delete("/api/config-data/{id}", configData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean configDataExistsInEs = configDataSearchRepository.exists(configData.getId());
        assertThat(configDataExistsInEs).isFalse();

        // Validate the database is empty
        List<ConfigData> configDataList = configDataRepository.findAll();
        assertThat(configDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchConfigData() throws Exception {
        // Initialize the database
        configDataService.save(configData);

        // Search the configData
        restConfigDataMockMvc.perform(get("/api/_search/config-data?query=id:" + configData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configData.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].encrypted").value(hasItem(DEFAULT_ENCRYPTED.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(sameInstant(DEFAULT_CREATED_ON))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(sameInstant(DEFAULT_UPDATED_ON))))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigData.class);
        ConfigData configData1 = new ConfigData();
        configData1.setId(1L);
        ConfigData configData2 = new ConfigData();
        configData2.setId(configData1.getId());
        assertThat(configData1).isEqualTo(configData2);
        configData2.setId(2L);
        assertThat(configData1).isNotEqualTo(configData2);
        configData1.setId(null);
        assertThat(configData1).isNotEqualTo(configData2);
    }
}
