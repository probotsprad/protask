package com.prolabs.web.rest;

import com.prolabs.SlackbotsApp;

import com.prolabs.domain.PermissionMst;
import com.prolabs.repository.PermissionMstRepository;
import com.prolabs.repository.search.PermissionMstSearchRepository;
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
 * Test class for the PermissionMstResource REST controller.
 *
 * @see PermissionMstResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackbotsApp.class)
public class PermissionMstResourceIntTest {

    private static final String DEFAULT_PRM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRM_DESC = "AAAAAAAAAA";
    private static final String UPDATED_PRM_DESC = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private PermissionMstRepository permissionMstRepository;

    @Autowired
    private PermissionMstSearchRepository permissionMstSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPermissionMstMockMvc;

    private PermissionMst permissionMst;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PermissionMstResource permissionMstResource = new PermissionMstResource(permissionMstRepository, permissionMstSearchRepository);
        this.restPermissionMstMockMvc = MockMvcBuilders.standaloneSetup(permissionMstResource)
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
    public static PermissionMst createEntity(EntityManager em) {
        PermissionMst permissionMst = new PermissionMst()
            .prmName(DEFAULT_PRM_NAME)
            .prmDesc(DEFAULT_PRM_DESC)
           // .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
        //    .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY);
        return permissionMst;
    }

    @Before
    public void initTest() {
        permissionMstSearchRepository.deleteAll();
        permissionMst = createEntity(em);
    }

    @Test
    @Transactional
    public void createPermissionMst() throws Exception {
        int databaseSizeBeforeCreate = permissionMstRepository.findAll().size();

        // Create the PermissionMst
        restPermissionMstMockMvc.perform(post("/api/permission-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permissionMst)))
            .andExpect(status().isCreated());

        // Validate the PermissionMst in the database
        List<PermissionMst> permissionMstList = permissionMstRepository.findAll();
        assertThat(permissionMstList).hasSize(databaseSizeBeforeCreate + 1);
        PermissionMst testPermissionMst = permissionMstList.get(permissionMstList.size() - 1);
        assertThat(testPermissionMst.getPrmName()).isEqualTo(DEFAULT_PRM_NAME);
        assertThat(testPermissionMst.getPrmDesc()).isEqualTo(DEFAULT_PRM_DESC);
        assertThat(testPermissionMst.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testPermissionMst.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPermissionMst.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testPermissionMst.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

        // Validate the PermissionMst in Elasticsearch
        PermissionMst permissionMstEs = permissionMstSearchRepository.findOne(testPermissionMst.getId());
        assertThat(permissionMstEs).isEqualToComparingFieldByField(testPermissionMst);
    }

    @Test
    @Transactional
    public void createPermissionMstWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = permissionMstRepository.findAll().size();

        // Create the PermissionMst with an existing ID
        permissionMst.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionMstMockMvc.perform(post("/api/permission-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permissionMst)))
            .andExpect(status().isBadRequest());

        // Validate the PermissionMst in the database
        List<PermissionMst> permissionMstList = permissionMstRepository.findAll();
        assertThat(permissionMstList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPrmNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionMstRepository.findAll().size();
        // set the field null
        permissionMst.setPrmName(null);

        // Create the PermissionMst, which fails.

        restPermissionMstMockMvc.perform(post("/api/permission-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permissionMst)))
            .andExpect(status().isBadRequest());

        List<PermissionMst> permissionMstList = permissionMstRepository.findAll();
        assertThat(permissionMstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPermissionMsts() throws Exception {
        // Initialize the database
        permissionMstRepository.saveAndFlush(permissionMst);

        // Get all the permissionMstList
        restPermissionMstMockMvc.perform(get("/api/permission-msts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].prmName").value(hasItem(DEFAULT_PRM_NAME.toString())))
            .andExpect(jsonPath("$.[*].prmDesc").value(hasItem(DEFAULT_PRM_DESC.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getPermissionMst() throws Exception {
        // Initialize the database
        permissionMstRepository.saveAndFlush(permissionMst);

        // Get the permissionMst
        restPermissionMstMockMvc.perform(get("/api/permission-msts/{id}", permissionMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(permissionMst.getId().intValue()))
            .andExpect(jsonPath("$.prmName").value(DEFAULT_PRM_NAME.toString()))
            .andExpect(jsonPath("$.prmDesc").value(DEFAULT_PRM_DESC.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPermissionMst() throws Exception {
        // Get the permissionMst
        restPermissionMstMockMvc.perform(get("/api/permission-msts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePermissionMst() throws Exception {
        // Initialize the database
        permissionMstRepository.saveAndFlush(permissionMst);
        permissionMstSearchRepository.save(permissionMst);
        int databaseSizeBeforeUpdate = permissionMstRepository.findAll().size();

        // Update the permissionMst
        PermissionMst updatedPermissionMst = permissionMstRepository.findOne(permissionMst.getId());
        // Disconnect from session so that the updates on updatedPermissionMst are not directly saved in db
        em.detach(updatedPermissionMst);
        updatedPermissionMst
            .prmName(UPDATED_PRM_NAME)
            .prmDesc(UPDATED_PRM_DESC)
         //   .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
      //      .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY);

        restPermissionMstMockMvc.perform(put("/api/permission-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPermissionMst)))
            .andExpect(status().isOk());

        // Validate the PermissionMst in the database
        List<PermissionMst> permissionMstList = permissionMstRepository.findAll();
        assertThat(permissionMstList).hasSize(databaseSizeBeforeUpdate);
        PermissionMst testPermissionMst = permissionMstList.get(permissionMstList.size() - 1);
        assertThat(testPermissionMst.getPrmName()).isEqualTo(UPDATED_PRM_NAME);
        assertThat(testPermissionMst.getPrmDesc()).isEqualTo(UPDATED_PRM_DESC);
        assertThat(testPermissionMst.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testPermissionMst.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPermissionMst.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testPermissionMst.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

        // Validate the PermissionMst in Elasticsearch
        PermissionMst permissionMstEs = permissionMstSearchRepository.findOne(testPermissionMst.getId());
        assertThat(permissionMstEs).isEqualToComparingFieldByField(testPermissionMst);
    }

    @Test
    @Transactional
    public void updateNonExistingPermissionMst() throws Exception {
        int databaseSizeBeforeUpdate = permissionMstRepository.findAll().size();

        // Create the PermissionMst

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPermissionMstMockMvc.perform(put("/api/permission-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(permissionMst)))
            .andExpect(status().isCreated());

        // Validate the PermissionMst in the database
        List<PermissionMst> permissionMstList = permissionMstRepository.findAll();
        assertThat(permissionMstList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePermissionMst() throws Exception {
        // Initialize the database
        permissionMstRepository.saveAndFlush(permissionMst);
        permissionMstSearchRepository.save(permissionMst);
        int databaseSizeBeforeDelete = permissionMstRepository.findAll().size();

        // Get the permissionMst
        restPermissionMstMockMvc.perform(delete("/api/permission-msts/{id}", permissionMst.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean permissionMstExistsInEs = permissionMstSearchRepository.exists(permissionMst.getId());
        assertThat(permissionMstExistsInEs).isFalse();

        // Validate the database is empty
        List<PermissionMst> permissionMstList = permissionMstRepository.findAll();
        assertThat(permissionMstList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPermissionMst() throws Exception {
        // Initialize the database
        permissionMstRepository.saveAndFlush(permissionMst);
        permissionMstSearchRepository.save(permissionMst);

        // Search the permissionMst
        restPermissionMstMockMvc.perform(get("/api/_search/permission-msts?query=id:" + permissionMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].prmName").value(hasItem(DEFAULT_PRM_NAME.toString())))
            .andExpect(jsonPath("$.[*].prmDesc").value(hasItem(DEFAULT_PRM_DESC.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissionMst.class);
        PermissionMst permissionMst1 = new PermissionMst();
        permissionMst1.setId(1L);
        PermissionMst permissionMst2 = new PermissionMst();
        permissionMst2.setId(permissionMst1.getId());
        assertThat(permissionMst1).isEqualTo(permissionMst2);
        permissionMst2.setId(2L);
        assertThat(permissionMst1).isNotEqualTo(permissionMst2);
        permissionMst1.setId(null);
        assertThat(permissionMst1).isNotEqualTo(permissionMst2);
    }
}
