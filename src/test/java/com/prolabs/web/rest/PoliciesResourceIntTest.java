package com.prolabs.web.rest;

import com.prolabs.SlackbotsApp;

import com.prolabs.domain.Policies;
import com.prolabs.repository.PoliciesRepository;
import com.prolabs.repository.search.PoliciesSearchRepository;
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
import java.util.List;

import static com.prolabs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PoliciesResource REST controller.
 *
 * @see PoliciesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackbotsApp.class)
public class PoliciesResourceIntTest {

    private static final String DEFAULT_POLICY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_POLICY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POLICY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_POLICY_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PoliciesRepository policiesRepository;

    @Autowired
    private PoliciesSearchRepository policiesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPoliciesMockMvc;

    private Policies policies;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PoliciesResource policiesResource = new PoliciesResource(policiesRepository, policiesSearchRepository);
        this.restPoliciesMockMvc = MockMvcBuilders.standaloneSetup(policiesResource)
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
    public static Policies createEntity(EntityManager em) {
        Policies policies = new Policies()
            .policyName(DEFAULT_POLICY_NAME)
            .policyDescription(DEFAULT_POLICY_DESCRIPTION);
        return policies;
    }

    @Before
    public void initTest() {
        policiesSearchRepository.deleteAll();
        policies = createEntity(em);
    }

    @Test
    @Transactional
    public void createPolicies() throws Exception {
        int databaseSizeBeforeCreate = policiesRepository.findAll().size();

        // Create the Policies
        restPoliciesMockMvc.perform(post("/api/policies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(policies)))
            .andExpect(status().isCreated());

        // Validate the Policies in the database
        List<Policies> policiesList = policiesRepository.findAll();
        assertThat(policiesList).hasSize(databaseSizeBeforeCreate + 1);
        Policies testPolicies = policiesList.get(policiesList.size() - 1);
        assertThat(testPolicies.getPolicyName()).isEqualTo(DEFAULT_POLICY_NAME);
        assertThat(testPolicies.getPolicyDescription()).isEqualTo(DEFAULT_POLICY_DESCRIPTION);

        // Validate the Policies in Elasticsearch
        Policies policiesEs = policiesSearchRepository.findOne(testPolicies.getId());
        assertThat(policiesEs).isEqualToComparingFieldByField(testPolicies);
    }

    @Test
    @Transactional
    public void createPoliciesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = policiesRepository.findAll().size();

        // Create the Policies with an existing ID
        policies.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoliciesMockMvc.perform(post("/api/policies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(policies)))
            .andExpect(status().isBadRequest());

        // Validate the Policies in the database
        List<Policies> policiesList = policiesRepository.findAll();
        assertThat(policiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPolicies() throws Exception {
        // Initialize the database
        policiesRepository.saveAndFlush(policies);

        // Get all the policiesList
        restPoliciesMockMvc.perform(get("/api/policies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policies.getId().intValue())))
            .andExpect(jsonPath("$.[*].policyName").value(hasItem(DEFAULT_POLICY_NAME.toString())))
            .andExpect(jsonPath("$.[*].policyDescription").value(hasItem(DEFAULT_POLICY_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPolicies() throws Exception {
        // Initialize the database
        policiesRepository.saveAndFlush(policies);

        // Get the policies
        restPoliciesMockMvc.perform(get("/api/policies/{id}", policies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(policies.getId().intValue()))
            .andExpect(jsonPath("$.policyName").value(DEFAULT_POLICY_NAME.toString()))
            .andExpect(jsonPath("$.policyDescription").value(DEFAULT_POLICY_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPolicies() throws Exception {
        // Get the policies
        restPoliciesMockMvc.perform(get("/api/policies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePolicies() throws Exception {
        // Initialize the database
        policiesRepository.saveAndFlush(policies);
        policiesSearchRepository.save(policies);
        int databaseSizeBeforeUpdate = policiesRepository.findAll().size();

        // Update the policies
        Policies updatedPolicies = policiesRepository.findOne(policies.getId());
        // Disconnect from session so that the updates on updatedPolicies are not directly saved in db
        em.detach(updatedPolicies);
        updatedPolicies
            .policyName(UPDATED_POLICY_NAME)
            .policyDescription(UPDATED_POLICY_DESCRIPTION);

        restPoliciesMockMvc.perform(put("/api/policies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPolicies)))
            .andExpect(status().isOk());

        // Validate the Policies in the database
        List<Policies> policiesList = policiesRepository.findAll();
        assertThat(policiesList).hasSize(databaseSizeBeforeUpdate);
        Policies testPolicies = policiesList.get(policiesList.size() - 1);
        assertThat(testPolicies.getPolicyName()).isEqualTo(UPDATED_POLICY_NAME);
        assertThat(testPolicies.getPolicyDescription()).isEqualTo(UPDATED_POLICY_DESCRIPTION);

        // Validate the Policies in Elasticsearch
        Policies policiesEs = policiesSearchRepository.findOne(testPolicies.getId());
        assertThat(policiesEs).isEqualToComparingFieldByField(testPolicies);
    }

    @Test
    @Transactional
    public void updateNonExistingPolicies() throws Exception {
        int databaseSizeBeforeUpdate = policiesRepository.findAll().size();

        // Create the Policies

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPoliciesMockMvc.perform(put("/api/policies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(policies)))
            .andExpect(status().isCreated());

        // Validate the Policies in the database
        List<Policies> policiesList = policiesRepository.findAll();
        assertThat(policiesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePolicies() throws Exception {
        // Initialize the database
        policiesRepository.saveAndFlush(policies);
        policiesSearchRepository.save(policies);
        int databaseSizeBeforeDelete = policiesRepository.findAll().size();

        // Get the policies
        restPoliciesMockMvc.perform(delete("/api/policies/{id}", policies.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean policiesExistsInEs = policiesSearchRepository.exists(policies.getId());
        assertThat(policiesExistsInEs).isFalse();

        // Validate the database is empty
        List<Policies> policiesList = policiesRepository.findAll();
        assertThat(policiesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPolicies() throws Exception {
        // Initialize the database
        policiesRepository.saveAndFlush(policies);
        policiesSearchRepository.save(policies);

        // Search the policies
        restPoliciesMockMvc.perform(get("/api/_search/policies?query=id:" + policies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policies.getId().intValue())))
            .andExpect(jsonPath("$.[*].policyName").value(hasItem(DEFAULT_POLICY_NAME.toString())))
            .andExpect(jsonPath("$.[*].policyDescription").value(hasItem(DEFAULT_POLICY_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Policies.class);
        Policies policies1 = new Policies();
        policies1.setId(1L);
        Policies policies2 = new Policies();
        policies2.setId(policies1.getId());
        assertThat(policies1).isEqualTo(policies2);
        policies2.setId(2L);
        assertThat(policies1).isNotEqualTo(policies2);
        policies1.setId(null);
        assertThat(policies1).isNotEqualTo(policies2);
    }
}
