package com.prolabs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.prolabs.domain.Tags;

import com.prolabs.repository.TagsRepository;
import com.prolabs.repository.search.TagsSearchRepository;
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
 * REST controller for managing Tags.
 */
@RestController
@RequestMapping("/api")
public class TagsResource {

    private final Logger log = LoggerFactory.getLogger(TagsResource.class);

    private static final String ENTITY_NAME = "tags";

    private final TagsRepository tagsRepository;

    private final TagsSearchRepository tagsSearchRepository;

    public TagsResource(TagsRepository tagsRepository, TagsSearchRepository tagsSearchRepository) {
        this.tagsRepository = tagsRepository;
        this.tagsSearchRepository = tagsSearchRepository;
    }

    /**
     * POST  /tags : Create a new tags.
     *
     * @param tags the tags to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tags, or with status 400 (Bad Request) if the tags has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tags")
    @Timed
    public ResponseEntity<Tags> createTags(@RequestBody Tags tags) throws URISyntaxException {
        log.debug("REST request to save Tags : {}", tags);
        if (tags.getId() != null) {
            throw new BadRequestAlertException("A new tags cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tags result = tagsRepository.save(tags);
        tagsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tags : Updates an existing tags.
     *
     * @param tags the tags to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tags,
     * or with status 400 (Bad Request) if the tags is not valid,
     * or with status 500 (Internal Server Error) if the tags couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tags")
    @Timed
    public ResponseEntity<Tags> updateTags(@RequestBody Tags tags) throws URISyntaxException {
        log.debug("REST request to update Tags : {}", tags);
        if (tags.getId() == null) {
            return createTags(tags);
        }
        Tags result = tagsRepository.save(tags);
        tagsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tags.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tags : get all the tags.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tags in body
     */
    @GetMapping("/tags")
    @Timed
    public List<Tags> getAllTags() {
        log.debug("REST request to get all Tags");
        return tagsRepository.findAll();
        }

    /**
     * GET  /tags/:id : get the "id" tags.
     *
     * @param id the id of the tags to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tags, or with status 404 (Not Found)
     */
    @GetMapping("/tags/{id}")
    @Timed
    public ResponseEntity<Tags> getTags(@PathVariable Long id) {
        log.debug("REST request to get Tags : {}", id);
        Tags tags = tagsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tags));
    }

    /**
     * DELETE  /tags/:id : delete the "id" tags.
     *
     * @param id the id of the tags to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tags/{id}")
    @Timed
    public ResponseEntity<Void> deleteTags(@PathVariable Long id) {
        log.debug("REST request to delete Tags : {}", id);
        tagsRepository.delete(id);
        tagsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tags?query=:query : search for the tags corresponding
     * to the query.
     *
     * @param query the query of the tags search
     * @return the result of the search
     */
    @GetMapping("/_search/tags")
    @Timed
    public List<Tags> searchTags(@RequestParam String query) {
        log.debug("REST request to search Tags for query {}", query);
        return StreamSupport
            .stream(tagsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
