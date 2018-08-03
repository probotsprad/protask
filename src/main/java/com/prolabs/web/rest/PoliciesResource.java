package com.prolabs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.prolabs.domain.Policies;

import com.prolabs.repository.PoliciesRepository;
import com.prolabs.repository.search.PoliciesSearchRepository;
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
 * REST controller for managing Policies.
 */
@RestController
@RequestMapping("/api")
public class PoliciesResource {

    private final Logger log = LoggerFactory.getLogger(PoliciesResource.class);

    private static final String ENTITY_NAME = "policies";

    private final PoliciesRepository policiesRepository;

    private final PoliciesSearchRepository policiesSearchRepository;

    public PoliciesResource(PoliciesRepository policiesRepository, PoliciesSearchRepository policiesSearchRepository) {
        this.policiesRepository = policiesRepository;
        this.policiesSearchRepository = policiesSearchRepository;
    }

    /**
     * POST  /policies : Create a new policies.
     *
     * @param policies the policies to create
     * @return the ResponseEntity with status 201 (Created) and with body the new policies, or with status 400 (Bad Request) if the policies has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/policies")
    @Timed
    public ResponseEntity<Policies> createPolicies(@RequestBody Policies policies) throws URISyntaxException {
        log.debug("REST request to save Policies : {}", policies);
        if (policies.getId() != null) {
            throw new BadRequestAlertException("A new policies cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Policies result = policiesRepository.save(policies);
        policiesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/policies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /policies : Updates an existing policies.
     *
     * @param policies the policies to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated policies,
     * or with status 400 (Bad Request) if the policies is not valid,
     * or with status 500 (Internal Server Error) if the policies couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/policies")
    @Timed
    public ResponseEntity<Policies> updatePolicies(@RequestBody Policies policies) throws URISyntaxException {
        log.debug("REST request to update Policies : {}", policies);
        if (policies.getId() == null) {
            return createPolicies(policies);
        }
        Policies result = policiesRepository.save(policies);
        policiesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, policies.getId().toString()))
            .body(result);
    }

    /**
     * GET  /policies : get all the policies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of policies in body
     */
    @GetMapping("/policies")
    @Timed
    public List<Policies> getAllPolicies() {
        log.debug("REST request to get all Policies");
        return policiesRepository.findAll();
        }

    /**
     * GET  /policies/:id : get the "id" policies.
     *
     * @param id the id of the policies to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the policies, or with status 404 (Not Found)
     */
    @GetMapping("/policies/{id}")
    @Timed
    public ResponseEntity<Policies> getPolicies(@PathVariable Long id) {
        log.debug("REST request to get Policies : {}", id);
        Policies policies = policiesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(policies));
    }

    /**
     * DELETE  /policies/:id : delete the "id" policies.
     *
     * @param id the id of the policies to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/policies/{id}")
    @Timed
    public ResponseEntity<Void> deletePolicies(@PathVariable Long id) {
        log.debug("REST request to delete Policies : {}", id);
        policiesRepository.delete(id);
        policiesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/policies?query=:query : search for the policies corresponding
     * to the query.
     *
     * @param query the query of the policies search
     * @return the result of the search
     */
    @GetMapping("/_search/policies")
    @Timed
    public List<Policies> searchPolicies(@RequestParam String query) {
        log.debug("REST request to search Policies for query {}", query);
        return StreamSupport
            .stream(policiesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
