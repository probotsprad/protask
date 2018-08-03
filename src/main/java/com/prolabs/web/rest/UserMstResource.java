package com.prolabs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.prolabs.domain.UserMst;
import com.prolabs.repository.UserMstRepository;
import com.prolabs.repository.search.UserMstSearchRepository;
import com.prolabs.service.ConfigDataService;
import com.prolabs.web.rest.errors.BadRequestAlertException;
import com.prolabs.web.rest.util.HeaderUtil;
import com.prolabs.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing UserMst.
 */
@RestController
@RequestMapping("/api")
public class UserMstResource {

    private final Logger log = LoggerFactory.getLogger(UserMstResource.class);

    private static final String ENTITY_NAME = "userMst";

    private final UserMstRepository userMstRepository;

    private final UserMstSearchRepository userMstSearchRepository;
    
    @Autowired
	private  ConfigDataService configDataService;
    
  


    public UserMstResource(UserMstRepository userMstRepository, UserMstSearchRepository userMstSearchRepository) {
        this.userMstRepository = userMstRepository;
        this.userMstSearchRepository = userMstSearchRepository;
    }

    /**
     * POST  /user-msts : Create a new userMst.
     *
     * @param userMst the userMst to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userMst, or with status 400 (Bad Request) if the userMst has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-msts")
    @Timed
    public ResponseEntity<UserMst> createUserMst(@Valid @RequestBody UserMst userMst) throws URISyntaxException {
        log.debug("REST request to save UserMst : {}", userMst);
        if (userMst.getId() != null) {
            throw new BadRequestAlertException("A new userMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserMst result = userMstRepository.save(userMst);
        userMstSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/user-msts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-msts : Updates an existing userMst.
     *
     * @param userMst the userMst to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userMst,
     * or with status 400 (Bad Request) if the userMst is not valid,
     * or with status 500 (Internal Server Error) if the userMst couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-msts")
    @Timed
    public ResponseEntity<UserMst> updateUserMst(@Valid @RequestBody UserMst userMst) throws URISyntaxException {
        log.debug("REST request to update UserMst : {}", userMst);
        if (userMst.getId() == null) {
            return createUserMst(userMst);
        }
        UserMst result = userMstRepository.save(userMst);
        userMstSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userMst.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-msts : get all the userMsts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userMsts in body
     */
    @GetMapping("/user-msts")
    @Timed
    public ResponseEntity<List<UserMst>> getAllUserMsts(Pageable pageable) {
        log.debug("REST request to get a page of UserMsts");
        Page<UserMst> page = userMstRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-msts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-msts/:id : get the "id" userMst.
     *
     * @param id the id of the userMst to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userMst, or with status 404 (Not Found)
     */
    @GetMapping("/user-msts/{id}")
    @Timed
    public ResponseEntity<UserMst> getUserMst(@PathVariable Long id) {
        log.debug("REST request to get UserMst : {}", id);
        UserMst userMst = userMstRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userMst));
    }

    /**
     * DELETE  /user-msts/:id : delete the "id" userMst.
     *
     * @param id the id of the userMst to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-msts/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserMst(@PathVariable Long id) {
        log.debug("REST request to delete UserMst : {}", id);
        userMstRepository.delete(id);
        userMstSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-msts?query=:query : search for the userMst corresponding
     * to the query.
     *
     * @param query the query of the userMst search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-msts")
    @Timed
    public ResponseEntity<List<UserMst>> searchUserMsts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UserMsts for query {}", query);
        Page<UserMst> page = userMstSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-msts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @PostMapping("/menuJson")
	@Timed
	public ResponseEntity<MenuItemsContainer> getMenuJson(@RequestBody String userLoginId) throws URISyntaxException {
    	Gson gson=new Gson();
		if(MenuJsonUtil.getMenuItemsContainer() == null) {
			
			MenuItemsContainer m = null;
			String json = configDataService.getMediusAdminMenuJson("medius_admin_menujson");
			if(json != null)
				m = gson.fromJson(json, MenuItemsContainer.class);
			MenuJsonUtil.setMenuItemsContainer(m);
		}

		MenuItemsContainer menuItemsContainer = null;
		if(UserUtil.getLogedInUser().equals("admin") || userLoginId.equalsIgnoreCase("admin")){
			menuItemsContainer = MenuJsonUtil.buildMenuJsonForAdmin();
			return ResponseUtil.wrapOrNotFound(Optional.ofNullable(menuItemsContainer));
		}
		if("admin".equalsIgnoreCase(userLoginId)){
			userLoginId = UserUtil.getLogedInUser();
		}
		UserMst userMst = configDataService.getUserByUserId(userLoginId);
		menuItemsContainer = MenuJsonUtil.buildMenuJson(userMst);
		log.info(userMst.getUserId());	
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(menuItemsContainer));
	}

}
