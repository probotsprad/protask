package com.prolabs.web.rest;

import com.prolabs.SlackbotsApp;

import com.prolabs.domain.SubTask;
import com.prolabs.repository.SubTaskRepository;
import com.prolabs.repository.search.SubTaskSearchRepository;
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
 * Test class for the SubTaskResource REST controller.
 *
 * @see SubTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackbotsApp.class)
public class SubTaskResourceIntTest {

    private static final String DEFAULT_TASK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TASK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TASK_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TASK_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private SubTaskSearchRepository subTaskSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubTaskMockMvc;

    private SubTask subTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubTaskResource subTaskResource = new SubTaskResource(subTaskRepository, subTaskSearchRepository);
        this.restSubTaskMockMvc = MockMvcBuilders.standaloneSetup(subTaskResource)
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
    public static SubTask createEntity(EntityManager em) {
        SubTask subTask = new SubTask()
            .taskName(DEFAULT_TASK_NAME)
            .taskDescription(DEFAULT_TASK_DESCRIPTION);
        return subTask;
    }

    @Before
    public void initTest() {
        subTaskSearchRepository.deleteAll();
        subTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubTask() throws Exception {
        int databaseSizeBeforeCreate = subTaskRepository.findAll().size();

        // Create the SubTask
        restSubTaskMockMvc.perform(post("/api/sub-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTask)))
            .andExpect(status().isCreated());

        // Validate the SubTask in the database
        List<SubTask> subTaskList = subTaskRepository.findAll();
        assertThat(subTaskList).hasSize(databaseSizeBeforeCreate + 1);
        SubTask testSubTask = subTaskList.get(subTaskList.size() - 1);
        assertThat(testSubTask.getTaskName()).isEqualTo(DEFAULT_TASK_NAME);
        assertThat(testSubTask.getTaskDescription()).isEqualTo(DEFAULT_TASK_DESCRIPTION);

        // Validate the SubTask in Elasticsearch
        SubTask subTaskEs = subTaskSearchRepository.findOne(testSubTask.getId());
        assertThat(subTaskEs).isEqualToComparingFieldByField(testSubTask);
    }

    @Test
    @Transactional
    public void createSubTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subTaskRepository.findAll().size();

        // Create the SubTask with an existing ID
        subTask.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubTaskMockMvc.perform(post("/api/sub-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTask)))
            .andExpect(status().isBadRequest());

        // Validate the SubTask in the database
        List<SubTask> subTaskList = subTaskRepository.findAll();
        assertThat(subTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSubTasks() throws Exception {
        // Initialize the database
        subTaskRepository.saveAndFlush(subTask);

        // Get all the subTaskList
        restSubTaskMockMvc.perform(get("/api/sub-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskName").value(hasItem(DEFAULT_TASK_NAME.toString())))
            .andExpect(jsonPath("$.[*].taskDescription").value(hasItem(DEFAULT_TASK_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSubTask() throws Exception {
        // Initialize the database
        subTaskRepository.saveAndFlush(subTask);

        // Get the subTask
        restSubTaskMockMvc.perform(get("/api/sub-tasks/{id}", subTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subTask.getId().intValue()))
            .andExpect(jsonPath("$.taskName").value(DEFAULT_TASK_NAME.toString()))
            .andExpect(jsonPath("$.taskDescription").value(DEFAULT_TASK_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSubTask() throws Exception {
        // Get the subTask
        restSubTaskMockMvc.perform(get("/api/sub-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubTask() throws Exception {
        // Initialize the database
        subTaskRepository.saveAndFlush(subTask);
        subTaskSearchRepository.save(subTask);
        int databaseSizeBeforeUpdate = subTaskRepository.findAll().size();

        // Update the subTask
        SubTask updatedSubTask = subTaskRepository.findOne(subTask.getId());
        // Disconnect from session so that the updates on updatedSubTask are not directly saved in db
        em.detach(updatedSubTask);
        updatedSubTask
            .taskName(UPDATED_TASK_NAME)
            .taskDescription(UPDATED_TASK_DESCRIPTION);

        restSubTaskMockMvc.perform(put("/api/sub-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubTask)))
            .andExpect(status().isOk());

        // Validate the SubTask in the database
        List<SubTask> subTaskList = subTaskRepository.findAll();
        assertThat(subTaskList).hasSize(databaseSizeBeforeUpdate);
        SubTask testSubTask = subTaskList.get(subTaskList.size() - 1);
        assertThat(testSubTask.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testSubTask.getTaskDescription()).isEqualTo(UPDATED_TASK_DESCRIPTION);

        // Validate the SubTask in Elasticsearch
        SubTask subTaskEs = subTaskSearchRepository.findOne(testSubTask.getId());
        assertThat(subTaskEs).isEqualToComparingFieldByField(testSubTask);
    }

    @Test
    @Transactional
    public void updateNonExistingSubTask() throws Exception {
        int databaseSizeBeforeUpdate = subTaskRepository.findAll().size();

        // Create the SubTask

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSubTaskMockMvc.perform(put("/api/sub-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subTask)))
            .andExpect(status().isCreated());

        // Validate the SubTask in the database
        List<SubTask> subTaskList = subTaskRepository.findAll();
        assertThat(subTaskList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSubTask() throws Exception {
        // Initialize the database
        subTaskRepository.saveAndFlush(subTask);
        subTaskSearchRepository.save(subTask);
        int databaseSizeBeforeDelete = subTaskRepository.findAll().size();

        // Get the subTask
        restSubTaskMockMvc.perform(delete("/api/sub-tasks/{id}", subTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean subTaskExistsInEs = subTaskSearchRepository.exists(subTask.getId());
        assertThat(subTaskExistsInEs).isFalse();

        // Validate the database is empty
        List<SubTask> subTaskList = subTaskRepository.findAll();
        assertThat(subTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSubTask() throws Exception {
        // Initialize the database
        subTaskRepository.saveAndFlush(subTask);
        subTaskSearchRepository.save(subTask);

        // Search the subTask
        restSubTaskMockMvc.perform(get("/api/_search/sub-tasks?query=id:" + subTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskName").value(hasItem(DEFAULT_TASK_NAME.toString())))
            .andExpect(jsonPath("$.[*].taskDescription").value(hasItem(DEFAULT_TASK_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubTask.class);
        SubTask subTask1 = new SubTask();
        subTask1.setId(1L);
        SubTask subTask2 = new SubTask();
        subTask2.setId(subTask1.getId());
        assertThat(subTask1).isEqualTo(subTask2);
        subTask2.setId(2L);
        assertThat(subTask1).isNotEqualTo(subTask2);
        subTask1.setId(null);
        assertThat(subTask1).isNotEqualTo(subTask2);
    }
}
