package com.prolabs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.prolabs.domain.SubTask;

import com.prolabs.repository.SubTaskRepository;
import com.prolabs.repository.search.SubTaskSearchRepository;
import com.prolabs.web.rest.errors.BadRequestAlertException;
import com.prolabs.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SubTask.
 */
@RestController
@RequestMapping("/api")
public class SubTaskResource {

    private final Logger log = LoggerFactory.getLogger(SubTaskResource.class);

    private static final String ENTITY_NAME = "subTask";

    private final SubTaskRepository subTaskRepository;

    private final SubTaskSearchRepository subTaskSearchRepository;

    public SubTaskResource(SubTaskRepository subTaskRepository, SubTaskSearchRepository subTaskSearchRepository) {
        this.subTaskRepository = subTaskRepository;
        this.subTaskSearchRepository = subTaskSearchRepository;
    }

    /**
     * POST  /sub-tasks : Create a new subTask.
     *
     * @param subTask the subTask to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subTask, or with status 400 (Bad Request) if the subTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sub-tasks")
    @Timed
    public ResponseEntity<SubTask> createSubTask(@RequestBody SubTask subTask) throws URISyntaxException {
        log.debug("REST request to save SubTask : {}", subTask);
        if (subTask.getId() != null) {
            throw new BadRequestAlertException("A new subTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubTask result = subTaskRepository.save(subTask);
        subTaskSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sub-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sub-tasks : Updates an existing subTask.
     *
     * @param subTask the subTask to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subTask,
     * or with status 400 (Bad Request) if the subTask is not valid,
     * or with status 500 (Internal Server Error) if the subTask couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sub-tasks")
    @Timed
    public ResponseEntity<SubTask> updateSubTask(@RequestBody SubTask subTask) throws URISyntaxException {
        log.debug("REST request to update SubTask : {}", subTask);
        if (subTask.getId() == null) {
            return createSubTask(subTask);
        }
        SubTask result = subTaskRepository.save(subTask);
        subTaskSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subTask.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sub-tasks : get all the subTasks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of subTasks in body
     */
    @GetMapping("/sub-tasks")
    @Timed
    public List<SubTask> getAllSubTasks() {
        log.debug("REST request to get all SubTasks");
        return subTaskRepository.findAll();
        }

    /**
     * GET  /sub-tasks/:id : get the "id" subTask.
     *
     * @param id the id of the subTask to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subTask, or with status 404 (Not Found)
     */
    @GetMapping("/sub-tasks/{id}")
    @Timed
    public ResponseEntity<SubTask> getSubTask(@PathVariable Long id) {
        log.debug("REST request to get SubTask : {}", id);
        SubTask subTask = subTaskRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subTask));
    }

    /**
     * DELETE  /sub-tasks/:id : delete the "id" subTask.
     *
     * @param id the id of the subTask to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sub-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubTask(@PathVariable Long id) {
        log.debug("REST request to delete SubTask : {}", id);
        subTaskRepository.delete(id);
        subTaskSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sub-tasks?query=:query : search for the subTask corresponding
     * to the query.
     *
     * @param query the query of the subTask search
     * @return the result of the search
     */
    @GetMapping("/_search/sub-tasks")
    @Timed
    public List<SubTask> searchSubTasks(@RequestParam String query) {
        log.debug("REST request to search SubTasks for query {}", query);
        return StreamSupport
            .stream(subTaskSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
