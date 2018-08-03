package com.prolabs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.prolabs.domain.Context;

import com.prolabs.repository.ContextRepository;
import com.prolabs.repository.search.ContextSearchRepository;
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
 * REST controller for managing Context.
 */
@RestController
@RequestMapping("/api")
public class ContextResource {

    private final Logger log = LoggerFactory.getLogger(ContextResource.class);

    private static final String ENTITY_NAME = "context";

    private final ContextRepository contextRepository;

    private final ContextSearchRepository contextSearchRepository;

    public ContextResource(ContextRepository contextRepository, ContextSearchRepository contextSearchRepository) {
        this.contextRepository = contextRepository;
        this.contextSearchRepository = contextSearchRepository;
    }

    /**
     * POST  /contexts : Create a new context.
     *
     * @param context the context to create
     * @return the ResponseEntity with status 201 (Created) and with body the new context, or with status 400 (Bad Request) if the context has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contexts")
    @Timed
    public ResponseEntity<Context> createContext(@RequestBody Context context) throws URISyntaxException {
        log.debug("REST request to save Context : {}", context);
        if (context.getId() != null) {
            throw new BadRequestAlertException("A new context cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Context result = contextRepository.save(context);
        contextSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/contexts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contexts : Updates an existing context.
     *
     * @param context the context to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated context,
     * or with status 400 (Bad Request) if the context is not valid,
     * or with status 500 (Internal Server Error) if the context couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contexts")
    @Timed
    public ResponseEntity<Context> updateContext(@RequestBody Context context) throws URISyntaxException {
        log.debug("REST request to update Context : {}", context);
        if (context.getId() == null) {
            return createContext(context);
        }
        Context result = contextRepository.save(context);
        contextSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, context.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contexts : get all the contexts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contexts in body
     */
    @GetMapping("/contexts")
    @Timed
    public List<Context> getAllContexts() {
        log.debug("REST request to get all Contexts");
        return contextRepository.findAll();
        }

    /**
     * GET  /contexts/:id : get the "id" context.
     *
     * @param id the id of the context to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the context, or with status 404 (Not Found)
     */
    @GetMapping("/contexts/{id}")
    @Timed
    public ResponseEntity<Context> getContext(@PathVariable Long id) {
        log.debug("REST request to get Context : {}", id);
        Context context = contextRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(context));
    }

    /**
     * DELETE  /contexts/:id : delete the "id" context.
     *
     * @param id the id of the context to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contexts/{id}")
    @Timed
    public ResponseEntity<Void> deleteContext(@PathVariable Long id) {
        log.debug("REST request to delete Context : {}", id);
        contextRepository.delete(id);
        contextSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contexts?query=:query : search for the context corresponding
     * to the query.
     *
     * @param query the query of the context search
     * @return the result of the search
     */
    @GetMapping("/_search/contexts")
    @Timed
    public List<Context> searchContexts(@RequestParam String query) {
        log.debug("REST request to search Contexts for query {}", query);
        return StreamSupport
            .stream(contextSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
