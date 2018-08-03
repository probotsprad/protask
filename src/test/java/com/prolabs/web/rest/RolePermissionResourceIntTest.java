package com.prolabs.web.rest;

import com.prolabs.SlackbotsApp;

import com.prolabs.domain.RolePermission;
import com.prolabs.repository.RolePermissionRepository;
import com.prolabs.repository.search.RolePermissionSearchRepository;
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
 * Test class for the RolePermissionResource REST controller.
 *
 * @see RolePermissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackbotsApp.class)
public class RolePermissionResourceIntTest {

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RolePermissionSearchRepository rolePermissionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRolePermissionMockMvc;

    private RolePermission rolePermission;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RolePermissionResource rolePermissionResource = new RolePermissionResource(rolePermissionRepository, rolePermissionSearchRepository);
        this.restRolePermissionMockMvc = MockMvcBuilders.standaloneSetup(rolePermissionResource)
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
    public static RolePermission createEntity(EntityManager em) {
        RolePermission rolePermission = new RolePermission()
      //      .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
     //       .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY);
        return rolePermission;
    }

    @Before
    public void initTest() {
        rolePermissionSearchRepository.deleteAll();
        rolePermission = createEntity(em);
    }

    @Test
    @Transactional
    public void createRolePermission() throws Exception {
        int databaseSizeBeforeCreate = rolePermissionRepository.findAll().size();

        // Create the RolePermission
        restRolePermissionMockMvc.perform(post("/api/role-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rolePermission)))
            .andExpect(status().isCreated());

        // Validate the RolePermission in the database
        List<RolePermission> rolePermissionList = rolePermissionRepository.findAll();
        assertThat(rolePermissionList).hasSize(databaseSizeBeforeCreate + 1);
        RolePermission testRolePermission = rolePermissionList.get(rolePermissionList.size() - 1);
        assertThat(testRolePermission.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRolePermission.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRolePermission.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRolePermission.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

        // Validate the RolePermission in Elasticsearch
        RolePermission rolePermissionEs = rolePermissionSearchRepository.findOne(testRolePermission.getId());
        assertThat(rolePermissionEs).isEqualToComparingFieldByField(testRolePermission);
    }

    @Test
    @Transactional
    public void createRolePermissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rolePermissionRepository.findAll().size();

        // Create the RolePermission with an existing ID
        rolePermission.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRolePermissionMockMvc.perform(post("/api/role-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rolePermission)))
            .andExpect(status().isBadRequest());

        // Validate the RolePermission in the database
        List<RolePermission> rolePermissionList = rolePermissionRepository.findAll();
        assertThat(rolePermissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRolePermissions() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissionList
        restRolePermissionMockMvc.perform(get("/api/role-permissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rolePermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getRolePermission() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get the rolePermission
        restRolePermissionMockMvc.perform(get("/api/role-permissions/{id}", rolePermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rolePermission.getId().intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRolePermission() throws Exception {
        // Get the rolePermission
        restRolePermissionMockMvc.perform(get("/api/role-permissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRolePermission() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);
        rolePermissionSearchRepository.save(rolePermission);
        int databaseSizeBeforeUpdate = rolePermissionRepository.findAll().size();

        // Update the rolePermission
        RolePermission updatedRolePermission = rolePermissionRepository.findOne(rolePermission.getId());
        // Disconnect from session so that the updates on updatedRolePermission are not directly saved in db
        em.detach(updatedRolePermission);
        updatedRolePermission
       //     .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
      //      .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY);

        restRolePermissionMockMvc.perform(put("/api/role-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRolePermission)))
            .andExpect(status().isOk());

        // Validate the RolePermission in the database
        List<RolePermission> rolePermissionList = rolePermissionRepository.findAll();
        assertThat(rolePermissionList).hasSize(databaseSizeBeforeUpdate);
        RolePermission testRolePermission = rolePermissionList.get(rolePermissionList.size() - 1);
        assertThat(testRolePermission.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRolePermission.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRolePermission.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRolePermission.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

        // Validate the RolePermission in Elasticsearch
        RolePermission rolePermissionEs = rolePermissionSearchRepository.findOne(testRolePermission.getId());
        assertThat(rolePermissionEs).isEqualToComparingFieldByField(testRolePermission);
    }

    @Test
    @Transactional
    public void updateNonExistingRolePermission() throws Exception {
        int databaseSizeBeforeUpdate = rolePermissionRepository.findAll().size();

        // Create the RolePermission

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRolePermissionMockMvc.perform(put("/api/role-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rolePermission)))
            .andExpect(status().isCreated());

        // Validate the RolePermission in the database
        List<RolePermission> rolePermissionList = rolePermissionRepository.findAll();
        assertThat(rolePermissionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRolePermission() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);
        rolePermissionSearchRepository.save(rolePermission);
        int databaseSizeBeforeDelete = rolePermissionRepository.findAll().size();

        // Get the rolePermission
        restRolePermissionMockMvc.perform(delete("/api/role-permissions/{id}", rolePermission.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean rolePermissionExistsInEs = rolePermissionSearchRepository.exists(rolePermission.getId());
        assertThat(rolePermissionExistsInEs).isFalse();

        // Validate the database is empty
        List<RolePermission> rolePermissionList = rolePermissionRepository.findAll();
        assertThat(rolePermissionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRolePermission() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);
        rolePermissionSearchRepository.save(rolePermission);

        // Search the rolePermission
        restRolePermissionMockMvc.perform(get("/api/_search/role-permissions?query=id:" + rolePermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rolePermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RolePermission.class);
        RolePermission rolePermission1 = new RolePermission();
        rolePermission1.setId(1L);
        RolePermission rolePermission2 = new RolePermission();
        rolePermission2.setId(rolePermission1.getId());
        assertThat(rolePermission1).isEqualTo(rolePermission2);
        rolePermission2.setId(2L);
        assertThat(rolePermission1).isNotEqualTo(rolePermission2);
        rolePermission1.setId(null);
        assertThat(rolePermission1).isNotEqualTo(rolePermission2);
    }
}
