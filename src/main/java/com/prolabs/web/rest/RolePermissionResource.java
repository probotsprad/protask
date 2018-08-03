package com.prolabs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.prolabs.domain.RolePermission;

import com.prolabs.repository.RolePermissionRepository;
import com.prolabs.repository.search.RolePermissionSearchRepository;
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
 * REST controller for managing RolePermission.
 */
@RestController
@RequestMapping("/api")
public class RolePermissionResource {

    private final Logger log = LoggerFactory.getLogger(RolePermissionResource.class);

    private static final String ENTITY_NAME = "rolePermission";

    private final RolePermissionRepository rolePermissionRepository;

    private final RolePermissionSearchRepository rolePermissionSearchRepository;

    public RolePermissionResource(RolePermissionRepository rolePermissionRepository, RolePermissionSearchRepository rolePermissionSearchRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionSearchRepository = rolePermissionSearchRepository;
    }

    /**
     * POST  /role-permissions : Create a new rolePermission.
     *
     * @param rolePermission the rolePermission to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rolePermission, or with status 400 (Bad Request) if the rolePermission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/role-permissions")
    @Timed
    public ResponseEntity<RolePermission> createRolePermission(@Valid @RequestBody RolePermission rolePermission) throws URISyntaxException {
        log.debug("REST request to save RolePermission : {}", rolePermission);
        if (rolePermission.getId() != null) {
            throw new BadRequestAlertException("A new rolePermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RolePermission result = rolePermissionRepository.save(rolePermission);
        rolePermissionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/role-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /role-permissions : Updates an existing rolePermission.
     *
     * @param rolePermission the rolePermission to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rolePermission,
     * or with status 400 (Bad Request) if the rolePermission is not valid,
     * or with status 500 (Internal Server Error) if the rolePermission couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/role-permissions")
    @Timed
    public ResponseEntity<RolePermission> updateRolePermission(@Valid @RequestBody RolePermission rolePermission) throws URISyntaxException {
        log.debug("REST request to update RolePermission : {}", rolePermission);
        if (rolePermission.getId() == null) {
            return createRolePermission(rolePermission);
        }
        RolePermission result = rolePermissionRepository.save(rolePermission);
        rolePermissionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rolePermission.getId().toString()))
            .body(result);
    }

    /**
     * GET  /role-permissions : get all the rolePermissions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rolePermissions in body
     */
    @GetMapping("/role-permissions")
    @Timed
    public ResponseEntity<List<RolePermission>> getAllRolePermissions(Pageable pageable) {
        log.debug("REST request to get a page of RolePermissions");
        Page<RolePermission> page = rolePermissionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/role-permissions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /role-permissions/:id : get the "id" rolePermission.
     *
     * @param id the id of the rolePermission to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rolePermission, or with status 404 (Not Found)
     */
    @GetMapping("/role-permissions/{id}")
    @Timed
    public ResponseEntity<RolePermission> getRolePermission(@PathVariable Long id) {
        log.debug("REST request to get RolePermission : {}", id);
        RolePermission rolePermission = rolePermissionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rolePermission));
    }

    /**
     * DELETE  /role-permissions/:id : delete the "id" rolePermission.
     *
     * @param id the id of the rolePermission to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/role-permissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteRolePermission(@PathVariable Long id) {
        log.debug("REST request to delete RolePermission : {}", id);
        rolePermissionRepository.delete(id);
        rolePermissionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/role-permissions?query=:query : search for the rolePermission corresponding
     * to the query.
     *
     * @param query the query of the rolePermission search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/role-permissions")
    @Timed
    public ResponseEntity<List<RolePermission>> searchRolePermissions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RolePermissions for query {}", query);
        Page<RolePermission> page = rolePermissionSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/role-permissions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
