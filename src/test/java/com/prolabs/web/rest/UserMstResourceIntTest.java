package com.prolabs.web.rest;

import com.prolabs.SlackbotsApp;

import com.prolabs.domain.UserMst;
import com.prolabs.repository.UserMstRepository;
import com.prolabs.repository.search.UserMstSearchRepository;
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

import com.prolabs.domain.enumeration.YesNoFlag;
/**
 * Test class for the UserMstResource REST controller.
 *
 * @see UserMstResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackbotsApp.class)
public class UserMstResourceIntTest {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_USER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final YesNoFlag DEFAULT_ACTIVE = YesNoFlag.Y;
    private static final YesNoFlag UPDATED_ACTIVE = YesNoFlag.N;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private UserMstRepository userMstRepository;

    @Autowired
    private UserMstSearchRepository userMstSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserMstMockMvc;

    private UserMst userMst;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserMstResource userMstResource = new UserMstResource(userMstRepository, userMstSearchRepository);
        this.restUserMstMockMvc = MockMvcBuilders.standaloneSetup(userMstResource)
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
    public static UserMst createEntity(EntityManager em) {
        UserMst userMst = new UserMst()
            .userId(DEFAULT_USER_ID)
            .userEmail(DEFAULT_USER_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .active(DEFAULT_ACTIVE)
     //       .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
    //        .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY);
        return userMst;
    }

    @Before
    public void initTest() {
        userMstSearchRepository.deleteAll();
        userMst = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserMst() throws Exception {
        int databaseSizeBeforeCreate = userMstRepository.findAll().size();

        // Create the UserMst
        restUserMstMockMvc.perform(post("/api/user-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMst)))
            .andExpect(status().isCreated());

        // Validate the UserMst in the database
        List<UserMst> userMstList = userMstRepository.findAll();
        assertThat(userMstList).hasSize(databaseSizeBeforeCreate + 1);
        UserMst testUserMst = userMstList.get(userMstList.size() - 1);
        assertThat(testUserMst.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserMst.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testUserMst.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserMst.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserMst.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testUserMst.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testUserMst.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserMst.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testUserMst.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

        // Validate the UserMst in Elasticsearch
        UserMst userMstEs = userMstSearchRepository.findOne(testUserMst.getId());
        assertThat(userMstEs).isEqualToComparingFieldByField(testUserMst);
    }

    @Test
    @Transactional
    public void createUserMstWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userMstRepository.findAll().size();

        // Create the UserMst with an existing ID
        userMst.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMstMockMvc.perform(post("/api/user-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMst)))
            .andExpect(status().isBadRequest());

        // Validate the UserMst in the database
        List<UserMst> userMstList = userMstRepository.findAll();
        assertThat(userMstList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMstRepository.findAll().size();
        // set the field null
        userMst.setUserId(null);

        // Create the UserMst, which fails.

        restUserMstMockMvc.perform(post("/api/user-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMst)))
            .andExpect(status().isBadRequest());

        List<UserMst> userMstList = userMstRepository.findAll();
        assertThat(userMstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMstRepository.findAll().size();
        // set the field null
        userMst.setUserEmail(null);

        // Create the UserMst, which fails.

        restUserMstMockMvc.perform(post("/api/user-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMst)))
            .andExpect(status().isBadRequest());

        List<UserMst> userMstList = userMstRepository.findAll();
        assertThat(userMstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMstRepository.findAll().size();
        // set the field null
        userMst.setFirstName(null);

        // Create the UserMst, which fails.

        restUserMstMockMvc.perform(post("/api/user-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMst)))
            .andExpect(status().isBadRequest());

        List<UserMst> userMstList = userMstRepository.findAll();
        assertThat(userMstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMstRepository.findAll().size();
        // set the field null
        userMst.setLastName(null);

        // Create the UserMst, which fails.

        restUserMstMockMvc.perform(post("/api/user-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMst)))
            .andExpect(status().isBadRequest());

        List<UserMst> userMstList = userMstRepository.findAll();
        assertThat(userMstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserMsts() throws Exception {
        // Initialize the database
        userMstRepository.saveAndFlush(userMst);

        // Get all the userMstList
        restUserMstMockMvc.perform(get("/api/user-msts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getUserMst() throws Exception {
        // Initialize the database
        userMstRepository.saveAndFlush(userMst);

        // Get the userMst
        restUserMstMockMvc.perform(get("/api/user-msts/{id}", userMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userMst.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.userEmail").value(DEFAULT_USER_EMAIL.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserMst() throws Exception {
        // Get the userMst
        restUserMstMockMvc.perform(get("/api/user-msts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserMst() throws Exception {
        // Initialize the database
        userMstRepository.saveAndFlush(userMst);
        userMstSearchRepository.save(userMst);
        int databaseSizeBeforeUpdate = userMstRepository.findAll().size();

        // Update the userMst
        UserMst updatedUserMst = userMstRepository.findOne(userMst.getId());
        // Disconnect from session so that the updates on updatedUserMst are not directly saved in db
        em.detach(updatedUserMst);
        updatedUserMst
            .userId(UPDATED_USER_ID)
            .userEmail(UPDATED_USER_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .active(UPDATED_ACTIVE)
        //    .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
        //    .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY);

        restUserMstMockMvc.perform(put("/api/user-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserMst)))
            .andExpect(status().isOk());

        // Validate the UserMst in the database
        List<UserMst> userMstList = userMstRepository.findAll();
        assertThat(userMstList).hasSize(databaseSizeBeforeUpdate);
        UserMst testUserMst = userMstList.get(userMstList.size() - 1);
        assertThat(testUserMst.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserMst.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testUserMst.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserMst.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserMst.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testUserMst.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testUserMst.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserMst.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testUserMst.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

        // Validate the UserMst in Elasticsearch
        UserMst userMstEs = userMstSearchRepository.findOne(testUserMst.getId());
        assertThat(userMstEs).isEqualToComparingFieldByField(testUserMst);
    }

    @Test
    @Transactional
    public void updateNonExistingUserMst() throws Exception {
        int databaseSizeBeforeUpdate = userMstRepository.findAll().size();

        // Create the UserMst

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserMstMockMvc.perform(put("/api/user-msts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMst)))
            .andExpect(status().isCreated());

        // Validate the UserMst in the database
        List<UserMst> userMstList = userMstRepository.findAll();
        assertThat(userMstList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserMst() throws Exception {
        // Initialize the database
        userMstRepository.saveAndFlush(userMst);
        userMstSearchRepository.save(userMst);
        int databaseSizeBeforeDelete = userMstRepository.findAll().size();

        // Get the userMst
        restUserMstMockMvc.perform(delete("/api/user-msts/{id}", userMst.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean userMstExistsInEs = userMstSearchRepository.exists(userMst.getId());
        assertThat(userMstExistsInEs).isFalse();

        // Validate the database is empty
        List<UserMst> userMstList = userMstRepository.findAll();
        assertThat(userMstList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUserMst() throws Exception {
        // Initialize the database
        userMstRepository.saveAndFlush(userMst);
        userMstSearchRepository.save(userMst);

        // Search the userMst
        restUserMstMockMvc.perform(get("/api/_search/user-msts?query=id:" + userMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMst.class);
        UserMst userMst1 = new UserMst();
        userMst1.setId(1L);
        UserMst userMst2 = new UserMst();
        userMst2.setId(userMst1.getId());
        assertThat(userMst1).isEqualTo(userMst2);
        userMst2.setId(2L);
        assertThat(userMst1).isNotEqualTo(userMst2);
        userMst1.setId(null);
        assertThat(userMst1).isNotEqualTo(userMst2);
    }
}
