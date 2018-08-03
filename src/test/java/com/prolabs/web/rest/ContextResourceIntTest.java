package com.prolabs.web.rest;

import com.prolabs.SlackbotsApp;

import com.prolabs.domain.Context;
import com.prolabs.repository.ContextRepository;
import com.prolabs.repository.search.ContextSearchRepository;
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
 * Test class for the ContextResource REST controller.
 *
 * @see ContextResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackbotsApp.class)
public class ContextResourceIntTest {

    private static final String DEFAULT_CONTEXT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTEXT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTEXT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CONTEXT_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private ContextSearchRepository contextSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContextMockMvc;

    private Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContextResource contextResource = new ContextResource(contextRepository, contextSearchRepository);
        this.restContextMockMvc = MockMvcBuilders.standaloneSetup(contextResource)
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
    public static Context createEntity(EntityManager em) {
        Context context = new Context()
            .contextName(DEFAULT_CONTEXT_NAME)
            .contextDescription(DEFAULT_CONTEXT_DESCRIPTION);
        return context;
    }

    @Before
    public void initTest() {
        contextSearchRepository.deleteAll();
        context = createEntity(em);
    }

    @Test
    @Transactional
    public void createContext() throws Exception {
        int databaseSizeBeforeCreate = contextRepository.findAll().size();

        // Create the Context
        restContextMockMvc.perform(post("/api/contexts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(context)))
            .andExpect(status().isCreated());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeCreate + 1);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getContextName()).isEqualTo(DEFAULT_CONTEXT_NAME);
        assertThat(testContext.getContextDescription()).isEqualTo(DEFAULT_CONTEXT_DESCRIPTION);

        // Validate the Context in Elasticsearch
        Context contextEs = contextSearchRepository.findOne(testContext.getId());
        assertThat(contextEs).isEqualToComparingFieldByField(testContext);
    }

    @Test
    @Transactional
    public void createContextWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contextRepository.findAll().size();

        // Create the Context with an existing ID
        context.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContextMockMvc.perform(post("/api/contexts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(context)))
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContexts() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        // Get all the contextList
        restContextMockMvc.perform(get("/api/contexts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(context.getId().intValue())))
            .andExpect(jsonPath("$.[*].contextName").value(hasItem(DEFAULT_CONTEXT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contextDescription").value(hasItem(DEFAULT_CONTEXT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        // Get the context
        restContextMockMvc.perform(get("/api/contexts/{id}", context.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(context.getId().intValue()))
            .andExpect(jsonPath("$.contextName").value(DEFAULT_CONTEXT_NAME.toString()))
            .andExpect(jsonPath("$.contextDescription").value(DEFAULT_CONTEXT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContext() throws Exception {
        // Get the context
        restContextMockMvc.perform(get("/api/contexts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);
        contextSearchRepository.save(context);
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();

        // Update the context
        Context updatedContext = contextRepository.findOne(context.getId());
        // Disconnect from session so that the updates on updatedContext are not directly saved in db
        em.detach(updatedContext);
        updatedContext
            .contextName(UPDATED_CONTEXT_NAME)
            .contextDescription(UPDATED_CONTEXT_DESCRIPTION);

        restContextMockMvc.perform(put("/api/contexts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContext)))
            .andExpect(status().isOk());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getContextName()).isEqualTo(UPDATED_CONTEXT_NAME);
        assertThat(testContext.getContextDescription()).isEqualTo(UPDATED_CONTEXT_DESCRIPTION);

        // Validate the Context in Elasticsearch
        Context contextEs = contextSearchRepository.findOne(testContext.getId());
        assertThat(contextEs).isEqualToComparingFieldByField(testContext);
    }

    @Test
    @Transactional
    public void updateNonExistingContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();

        // Create the Context

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContextMockMvc.perform(put("/api/contexts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(context)))
            .andExpect(status().isCreated());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);
        contextSearchRepository.save(context);
        int databaseSizeBeforeDelete = contextRepository.findAll().size();

        // Get the context
        restContextMockMvc.perform(delete("/api/contexts/{id}", context.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean contextExistsInEs = contextSearchRepository.exists(context.getId());
        assertThat(contextExistsInEs).isFalse();

        // Validate the database is empty
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);
        contextSearchRepository.save(context);

        // Search the context
        restContextMockMvc.perform(get("/api/_search/contexts?query=id:" + context.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(context.getId().intValue())))
            .andExpect(jsonPath("$.[*].contextName").value(hasItem(DEFAULT_CONTEXT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contextDescription").value(hasItem(DEFAULT_CONTEXT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Context.class);
        Context context1 = new Context();
        context1.setId(1L);
        Context context2 = new Context();
        context2.setId(context1.getId());
        assertThat(context1).isEqualTo(context2);
        context2.setId(2L);
        assertThat(context1).isNotEqualTo(context2);
        context1.setId(null);
        assertThat(context1).isNotEqualTo(context2);
    }
}
