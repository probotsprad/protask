package com.prolabs.web.rest;

import com.prolabs.SlackbotsApp;

import com.prolabs.domain.RoleMst;
import com.prolabs.repository.RoleMstRepository;
import com.prolabs.repository.search.RoleMstSearchRepository;
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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.prolabs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RoleMstResource REST controller.
 *
 * @see RoleMstResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackbotsApp.class)
public class RoleMstResourceIntTest {

    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE_DESC = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_DESC = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private RoleMstRepository roleMstRepository;

    @Autowired
    private RoleMstSearchRepository roleMstSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRoleMstMockMvc;

    private RoleMst roleMst;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoleMstResource roleMstResource = new RoleMstResource(roleMstRepository, roleMstSearchRepository);
        this.restRoleMstMockMvc = MockMvcBuilders.standaloneSetup(roleMstResource)
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
    public static RoleMst createEntity(EntityManager em) {
        RoleMst roleMst = new RoleMst()
            .roleName(DEFAULT_ROLE_NAME)
            .roleDesc(DEFAULT_ROLE_DESC)
         //   .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
         //   .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY);
        return roleMst;
    }

    @Before
    public void initTest() {
        roleMstSearchRepository.deleteAll();
        roleMst = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoleMst() throws Exception {
        int databaseSizeBeforeCreate = roleMstRepository.findAll().size();

        // Create the RoleMst
        restRoleMstMockMvc.perform(post("/api/role-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleMst)))
            .andExpect(status().isCreated());

        // Validate the RoleMst in the database
        List<RoleMst> roleMstList = roleMstRepository.findAll();
        assertThat(roleMstList).hasSize(databaseSizeBeforeCreate + 1);
        RoleMst testRoleMst = roleMstList.get(roleMstList.size() - 1);
        assertThat(testRoleMst.getRoleName()).isEqualTo(DEFAULT_ROLE_NAME);
        assertThat(testRoleMst.getRoleDesc()).isEqualTo(DEFAULT_ROLE_DESC);
        assertThat(testRoleMst.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRoleMst.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRoleMst.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRoleMst.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

        // Validate the RoleMst in Elasticsearch
        RoleMst roleMstEs = roleMstSearchRepository.findOne(testRoleMst.getId());
        assertThat(roleMstEs).isEqualToComparingFieldByField(testRoleMst);
    }

    @Test
    @Transactional
    public void createRoleMstWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roleMstRepository.findAll().size();

        // Create the RoleMst with an existing ID
        roleMst.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMstMockMvc.perform(post("/api/role-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleMst)))
            .andExpect(status().isBadRequest());

        // Validate the RoleMst in the database
        List<RoleMst> roleMstList = roleMstRepository.findAll();
        assertThat(roleMstList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRoleNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleMstRepository.findAll().size();
        // set the field null
        roleMst.setRoleName(null);

        // Create the RoleMst, which fails.

        restRoleMstMockMvc.perform(post("/api/role-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleMst)))
            .andExpect(status().isBadRequest());

        List<RoleMst> roleMstList = roleMstRepository.findAll();
        assertThat(roleMstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoleMsts() throws Exception {
        // Initialize the database
        roleMstRepository.saveAndFlush(roleMst);

        // Get all the roleMstList
        restRoleMstMockMvc.perform(get("/api/role-msts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].roleDesc").value(hasItem(DEFAULT_ROLE_DESC.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getRoleMst() throws Exception {
        // Initialize the database
        roleMstRepository.saveAndFlush(roleMst);

        // Get the roleMst
        restRoleMstMockMvc.perform(get("/api/role-msts/{id}", roleMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roleMst.getId().intValue()))
            .andExpect(jsonPath("$.roleName").value(DEFAULT_ROLE_NAME.toString()))
            .andExpect(jsonPath("$.roleDesc").value(DEFAULT_ROLE_DESC.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoleMst() throws Exception {
        // Get the roleMst
        restRoleMstMockMvc.perform(get("/api/role-msts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoleMst() throws Exception {
        // Initialize the database
        roleMstRepository.saveAndFlush(roleMst);
        roleMstSearchRepository.save(roleMst);
        int databaseSizeBeforeUpdate = roleMstRepository.findAll().size();

        // Update the roleMst
        RoleMst updatedRoleMst = roleMstRepository.findOne(roleMst.getId());
        // Disconnect from session so that the updates on updatedRoleMst are not directly saved in db
        em.detach(updatedRoleMst);
        updatedRoleMst
            .roleName(UPDATED_ROLE_NAME)
            .roleDesc(UPDATED_ROLE_DESC)
       //     .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
        //    .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY);

        restRoleMstMockMvc.perform(put("/api/role-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRoleMst)))
            .andExpect(status().isOk());

        // Validate the RoleMst in the database
        List<RoleMst> roleMstList = roleMstRepository.findAll();
        assertThat(roleMstList).hasSize(databaseSizeBeforeUpdate);
        RoleMst testRoleMst = roleMstList.get(roleMstList.size() - 1);
        assertThat(testRoleMst.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
        assertThat(testRoleMst.getRoleDesc()).isEqualTo(UPDATED_ROLE_DESC);
        assertThat(testRoleMst.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRoleMst.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRoleMst.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRoleMst.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

        // Validate the RoleMst in Elasticsearch
        RoleMst roleMstEs = roleMstSearchRepository.findOne(testRoleMst.getId());
        assertThat(roleMstEs).isEqualToComparingFieldByField(testRoleMst);
    }

    @Test
    @Transactional
    public void updateNonExistingRoleMst() throws Exception {
        int databaseSizeBeforeUpdate = roleMstRepository.findAll().size();

        // Create the RoleMst

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRoleMstMockMvc.perform(put("/api/role-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleMst)))
            .andExpect(status().isCreated());

        // Validate the RoleMst in the database
        List<RoleMst> roleMstList = roleMstRepository.findAll();
        assertThat(roleMstList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRoleMst() throws Exception {
        // Initialize the database
        roleMstRepository.saveAndFlush(roleMst);
        roleMstSearchRepository.save(roleMst);
        int databaseSizeBeforeDelete = roleMstRepository.findAll().size();

        // Get the roleMst
        restRoleMstMockMvc.perform(delete("/api/role-msts/{id}", roleMst.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean roleMstExistsInEs = roleMstSearchRepository.exists(roleMst.getId());
        assertThat(roleMstExistsInEs).isFalse();

        // Validate the database is empty
        List<RoleMst> roleMstList = roleMstRepository.findAll();
        assertThat(roleMstList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRoleMst() throws Exception {
        // Initialize the database
        roleMstRepository.saveAndFlush(roleMst);
        roleMstSearchRepository.save(roleMst);

        // Search the roleMst
        restRoleMstMockMvc.perform(get("/api/_search/role-msts?query=id:" + roleMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].roleDesc").value(hasItem(DEFAULT_ROLE_DESC.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleMst.class);
        RoleMst roleMst1 = new RoleMst();
        roleMst1.setId(1L);
        RoleMst roleMst2 = new RoleMst();
        roleMst2.setId(roleMst1.getId());
        assertThat(roleMst1).isEqualTo(roleMst2);
        roleMst2.setId(2L);
        assertThat(roleMst1).isNotEqualTo(roleMst2);
        roleMst1.setId(null);
        assertThat(roleMst1).isNotEqualTo(roleMst2);
    }
}
