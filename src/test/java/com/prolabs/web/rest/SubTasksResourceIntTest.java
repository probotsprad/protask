package com.prolabs.web.rest;

import com.prolabs.SlackbotsApp;

import com.prolabs.domain.SubTasks;
import com.prolabs.repository.SubTasksRepository;
import com.prolabs.repository.search.SubTasksSearchRepository;
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
 * Test class for the SubTasksResource REST controller.
 *
 * @see SubTasksResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackbotsApp.class)
public class SubTasksResourceIntTest {

    @Autowired
    private SubTasksRepository subTasksRepository;

    @Autowired
    private SubTasksSearchRepository subTasksSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubTasksMockMvc;

    private SubTasks subTasks;

    @Before
    public void setup() {
      /*  MockitoAnnotations.initMocks(this);
      //  final SubTasksResource subTasksResource = new SubTasksResource(subTasksRepository, subTasksSearchRepository);
        //this.restSubTasksMockMvc = MockMvcBuilders.standaloneSetup(subTasksResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();*/
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubTasks createEntity(EntityManager em) {
        SubTasks subTasks = new SubTasks();
        return subTasks;
    }

    @Before
    public void initTest() {
        subTasksSearchRepository.deleteAll();
        subTasks = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubTasks() throws Exception {
        int databaseSizeBeforeCreate = subTasksRepository.findAll().size();

        // Create the SubTasks
        restSubTasksMockMvc.perform(post("/api/sub-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTasks)))
            .andExpect(status().isCreated());

        // Validate the SubTasks in the database
        List<SubTasks> subTasksList = subTasksRepository.findAll();
        assertThat(subTasksList).hasSize(databaseSizeBeforeCreate + 1);
        SubTasks testSubTasks = subTasksList.get(subTasksList.size() - 1);

        // Validate the SubTasks in Elasticsearch
        SubTasks subTasksEs = subTasksSearchRepository.findOne(testSubTasks.getId());
        assertThat(subTasksEs).isEqualToComparingFieldByField(testSubTasks);
    }

    @Test
    @Transactional
    public void createSubTasksWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subTasksRepository.findAll().size();

        // Create the SubTasks with an existing ID
        subTasks.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubTasksMockMvc.perform(post("/api/sub-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTasks)))
            .andExpect(status().isBadRequest());

        // Validate the SubTasks in the database
        List<SubTasks> subTasksList = subTasksRepository.findAll();
        assertThat(subTasksList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSubTasks() throws Exception {
        // Initialize the database
        subTasksRepository.saveAndFlush(subTasks);

        // Get all the subTasksList
        restSubTasksMockMvc.perform(get("/api/sub-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subTasks.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSubTasks() throws Exception {
        // Initialize the database
        subTasksRepository.saveAndFlush(subTasks);

        // Get the subTasks
        restSubTasksMockMvc.perform(get("/api/sub-tasks/{id}", subTasks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subTasks.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSubTasks() throws Exception {
        // Get the subTasks
        restSubTasksMockMvc.perform(get("/api/sub-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubTasks() throws Exception {
        // Initialize the database
        subTasksRepository.saveAndFlush(subTasks);
        subTasksSearchRepository.save(subTasks);
        int databaseSizeBeforeUpdate = subTasksRepository.findAll().size();

        // Update the subTasks
        SubTasks updatedSubTasks = subTasksRepository.findOne(subTasks.getId());
        // Disconnect from session so that the updates on updatedSubTasks are not directly saved in db
        em.detach(updatedSubTasks);

        restSubTasksMockMvc.perform(put("/api/sub-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubTasks)))
            .andExpect(status().isOk());

        // Validate the SubTasks in the database
        List<SubTasks> subTasksList = subTasksRepository.findAll();
        assertThat(subTasksList).hasSize(databaseSizeBeforeUpdate);
        SubTasks testSubTasks = subTasksList.get(subTasksList.size() - 1);

        // Validate the SubTasks in Elasticsearch
        SubTasks subTasksEs = subTasksSearchRepository.findOne(testSubTasks.getId());
        assertThat(subTasksEs).isEqualToComparingFieldByField(testSubTasks);
    }

    @Test
    @Transactional
    public void updateNonExistingSubTasks() throws Exception {
        int databaseSizeBeforeUpdate = subTasksRepository.findAll().size();

        // Create the SubTasks

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSubTasksMockMvc.perform(put("/api/sub-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTasks)))
            .andExpect(status().isCreated());

        // Validate the SubTasks in the database
        List<SubTasks> subTasksList = subTasksRepository.findAll();
        assertThat(subTasksList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSubTasks() throws Exception {
        // Initialize the database
        subTasksRepository.saveAndFlush(subTasks);
        subTasksSearchRepository.save(subTasks);
        int databaseSizeBeforeDelete = subTasksRepository.findAll().size();

        // Get the subTasks
        restSubTasksMockMvc.perform(delete("/api/sub-tasks/{id}", subTasks.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean subTasksExistsInEs = subTasksSearchRepository.exists(subTasks.getId());
        assertThat(subTasksExistsInEs).isFalse();

        // Validate the database is empty
        List<SubTasks> subTasksList = subTasksRepository.findAll();
        assertThat(subTasksList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSubTasks() throws Exception {
        // Initialize the database
        subTasksRepository.saveAndFlush(subTasks);
        subTasksSearchRepository.save(subTasks);

        // Search the subTasks
        restSubTasksMockMvc.perform(get("/api/_search/sub-tasks?query=id:" + subTasks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subTasks.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubTasks.class);
        SubTasks subTasks1 = new SubTasks();
        subTasks1.setId(1L);
        SubTasks subTasks2 = new SubTasks();
        subTasks2.setId(subTasks1.getId());
        assertThat(subTasks1).isEqualTo(subTasks2);
        subTasks2.setId(2L);
        assertThat(subTasks1).isNotEqualTo(subTasks2);
        subTasks1.setId(null);
        assertThat(subTasks1).isNotEqualTo(subTasks2);
    }
}
