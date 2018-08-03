package com.prolabs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.prolabs.domain.RoleMst;

import com.prolabs.repository.RoleMstRepository;
import com.prolabs.repository.search.RoleMstSearchRepository;
import com.prolabs.web.rest.errors.BadRequestAlertException;
import com.prolabs.web.rest.util.HeaderUtil;
import com.prolabs.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RoleMst.
 */
@RestController
@RequestMapping("/api")
public class RoleMstResource {

    private final Logger log = LoggerFactory.getLogger(RoleMstResource.class);

    private static final String ENTITY_NAME = "roleMst";

    private final RoleMstRepository roleMstRepository;

    private final RoleMstSearchRepository roleMstSearchRepository;

    public RoleMstResource(RoleMstRepository roleMstRepository, RoleMstSearchRepository roleMstSearchRepository) {
        this.roleMstRepository = roleMstRepository;
        this.roleMstSearchRepository = roleMstSearchRepository;
    }

    /**
     * POST  /role-msts : Create a new roleMst.
     *
     * @param roleMst the roleMst to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roleMst, or with status 400 (Bad Request) if the roleMst has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/role-msts")
    @Timed
    public ResponseEntity<RoleMst> createRoleMst(@Valid @RequestBody RoleMst roleMst) throws URISyntaxException {
        log.debug("REST request to save RoleMst : {}", roleMst);
        if (roleMst.getId() != null) {
            throw new BadRequestAlertException("A new roleMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoleMst result = roleMstRepository.save(roleMst);
        roleMstSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/role-msts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /role-msts : Updates an existing roleMst.
     *
     * @param roleMst the roleMst to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roleMst,
     * or with status 400 (Bad Request) if the roleMst is not valid,
     * or with status 500 (Internal Server Error) if the roleMst couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/role-msts")
    @Timed
    public ResponseEntity<RoleMst> updateRoleMst(@Valid @RequestBody RoleMst roleMst) throws URISyntaxException {
        log.debug("REST request to update RoleMst : {}", roleMst);
        if (roleMst.getId() == null) {
            return createRoleMst(roleMst);
        }
        RoleMst result = roleMstRepository.save(roleMst);
        roleMstSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roleMst.getId().toString()))
            .body(result);
    }

    /**
     * GET  /role-msts : get all the roleMsts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roleMsts in body
     */
    @GetMapping("/role-msts")
    @Timed
    public ResponseEntity<List<RoleMst>> getAllRoleMsts(Pageable pageable) {
        log.debug("REST request to get a page of RoleMsts");
        Page<RoleMst> page = roleMstRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/role-msts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /role-msts/:id : get the "id" roleMst.
     *
     * @param id the id of the roleMst to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roleMst, or with status 404 (Not Found)
     */
    @GetMapping("/role-msts/{id}")
    @Timed
    public ResponseEntity<RoleMst> getRoleMst(@PathVariable Long id) {
        log.debug("REST request to get RoleMst : {}", id);
        RoleMst roleMst = roleMstRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(roleMst));
    }

    /**
     * DELETE  /role-msts/:id : delete the "id" roleMst.
     *
     * @param id the id of the roleMst to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/role-msts/{id}")
    @Timed
    public ResponseEntity<Void> deleteRoleMst(@PathVariable Long id) {
        log.debug("REST request to delete RoleMst : {}", id);
        roleMstRepository.delete(id);
        roleMstSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/role-msts?query=:query : search for the roleMst corresponding
     * to the query.
     *
     * @param query the query of the roleMst search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/role-msts")
    @Timed
    public ResponseEntity<List<RoleMst>> searchRoleMsts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RoleMsts for query {}", query);
        Page<RoleMst> page = roleMstSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/role-msts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
