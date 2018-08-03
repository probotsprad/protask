package com.prolabs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.prolabs.domain.UserRole;

import com.prolabs.repository.UserRoleRepository;
import com.prolabs.repository.search.UserRoleSearchRepository;
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
 * REST controller for managing UserRole.
 */
@RestController
@RequestMapping("/api")
public class UserRoleResource {

    private final Logger log = LoggerFactory.getLogger(UserRoleResource.class);

    private static final String ENTITY_NAME = "userRole";

    private final UserRoleRepository userRoleRepository;

    private final UserRoleSearchRepository userRoleSearchRepository;

    public UserRoleResource(UserRoleRepository userRoleRepository, UserRoleSearchRepository userRoleSearchRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleSearchRepository = userRoleSearchRepository;
    }

    /**
     * POST  /user-roles : Create a new userRole.
     *
     * @param userRole the userRole to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userRole, or with status 400 (Bad Request) if the userRole has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-roles")
    @Timed
    public ResponseEntity<UserRole> createUserRole(@Valid @RequestBody UserRole userRole) throws URISyntaxException {
        log.debug("REST request to save UserRole : {}", userRole);
        if (userRole.getId() != null) {
            throw new BadRequestAlertException("A new userRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserRole result = userRoleRepository.save(userRole);
        userRoleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/user-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-roles : Updates an existing userRole.
     *
     * @param userRole the userRole to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userRole,
     * or with status 400 (Bad Request) if the userRole is not valid,
     * or with status 500 (Internal Server Error) if the userRole couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-roles")
    @Timed
    public ResponseEntity<UserRole> updateUserRole(@Valid @RequestBody UserRole userRole) throws URISyntaxException {
        log.debug("REST request to update UserRole : {}", userRole);
        if (userRole.getId() == null) {
            return createUserRole(userRole);
        }
        UserRole result = userRoleRepository.save(userRole);
        userRoleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userRole.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-roles : get all the userRoles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userRoles in body
     */
    @GetMapping("/user-roles")
    @Timed
    public ResponseEntity<List<UserRole>> getAllUserRoles(Pageable pageable) {
        log.debug("REST request to get a page of UserRoles");
        Page<UserRole> page = userRoleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-roles/:id : get the "id" userRole.
     *
     * @param id the id of the userRole to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userRole, or with status 404 (Not Found)
     */
    @GetMapping("/user-roles/{id}")
    @Timed
    public ResponseEntity<UserRole> getUserRole(@PathVariable Long id) {
        log.debug("REST request to get UserRole : {}", id);
       UserRole userRole = null; // userRoleRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userRole));
    }

    /**
     * DELETE  /user-roles/:id : delete the "id" userRole.
     *
     * @param id the id of the userRole to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-roles/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id) {
        log.debug("REST request to delete UserRole : {}", id);
        userRoleRepository.delete(id);
        userRoleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-roles?query=:query : search for the userRole corresponding
     * to the query.
     *
     * @param query the query of the userRole search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-roles")
    @Timed
    public ResponseEntity<List<UserRole>> searchUserRoles(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UserRoles for query {}", query);
        Page<UserRole> page = userRoleSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
