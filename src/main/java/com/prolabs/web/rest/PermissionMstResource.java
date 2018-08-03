package com.prolabs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.prolabs.domain.PermissionMst;

import com.prolabs.repository.PermissionMstRepository;
import com.prolabs.repository.search.PermissionMstSearchRepository;
import com.prolabs.web.rest.errors.BadRequestAlertException;
import com.prolabs.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing PermissionMst.
 */
@RestController
@RequestMapping("/api")
public class PermissionMstResource {

    private final Logger log = LoggerFactory.getLogger(PermissionMstResource.class);

    private static final String ENTITY_NAME = "permissionMst";

    private final PermissionMstRepository permissionMstRepository;

    private final PermissionMstSearchRepository permissionMstSearchRepository;

    public PermissionMstResource(PermissionMstRepository permissionMstRepository, PermissionMstSearchRepository permissionMstSearchRepository) {
        this.permissionMstRepository = permissionMstRepository;
        this.permissionMstSearchRepository = permissionMstSearchRepository;
    }

    /**
     * POST  /permission-msts : Create a new permissionMst.
     *
     * @param permissionMst the permissionMst to create
     * @return the ResponseEntity with status 201 (Created) and with body the new permissionMst, or with status 400 (Bad Request) if the permissionMst has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/permission-msts")
    @Timed
    public ResponseEntity<PermissionMst> createPermissionMst(@Valid @RequestBody PermissionMst permissionMst) throws URISyntaxException {
        log.debug("REST request to save PermissionMst : {}", permissionMst);
        if (permissionMst.getId() != null) {
            throw new BadRequestAlertException("A new permissionMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PermissionMst result = permissionMstRepository.save(permissionMst);
        permissionMstSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/permission-msts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /permission-msts : Updates an existing permissionMst.
     *
     * @param permissionMst the permissionMst to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated permissionMst,
     * or with status 400 (Bad Request) if the permissionMst is not valid,
     * or with status 500 (Internal Server Error) if the permissionMst couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/permission-msts")
    @Timed
    public ResponseEntity<PermissionMst> updatePermissionMst(@Valid @RequestBody PermissionMst permissionMst) throws URISyntaxException {
        log.debug("REST request to update PermissionMst : {}", permissionMst);
        if (permissionMst.getId() == null) {
            return createPermissionMst(permissionMst);
        }
        PermissionMst result = permissionMstRepository.save(permissionMst);
        permissionMstSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, permissionMst.getId().toString()))
            .body(result);
    }

    /**
     * GET  /permission-msts : get all the permissionMsts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of permissionMsts in body
     */
    @GetMapping("/permission-msts")
    @Timed
    public List<PermissionMst> getAllPermissionMsts() {
        log.debug("REST request to get all PermissionMsts");
        return permissionMstRepository.findAll();
        }

    /**
     * GET  /permission-msts/:id : get the "id" permissionMst.
     *
     * @param id the id of the permissionMst to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the permissionMst, or with status 404 (Not Found)
     */
    @GetMapping("/permission-msts/{id}")
    @Timed
    public ResponseEntity<PermissionMst> getPermissionMst(@PathVariable Long id) {
        log.debug("REST request to get PermissionMst : {}", id);
        PermissionMst permissionMst = permissionMstRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(permissionMst));
    }

    /**
     * DELETE  /permission-msts/:id : delete the "id" permissionMst.
     *
     * @param id the id of the permissionMst to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/permission-msts/{id}")
    @Timed
    public ResponseEntity<Void> deletePermissionMst(@PathVariable Long id) {
        log.debug("REST request to delete PermissionMst : {}", id);
        permissionMstRepository.delete(id);
        permissionMstSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/permission-msts?query=:query : search for the permissionMst corresponding
     * to the query.
     *
     * @param query the query of the permissionMst search
     * @return the result of the search
     */
    @GetMapping("/_search/permission-msts")
    @Timed
    public List<PermissionMst> searchPermissionMsts(@RequestParam String query) {
        log.debug("REST request to search PermissionMsts for query {}", query);
        return StreamSupport
            .stream(permissionMstSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
