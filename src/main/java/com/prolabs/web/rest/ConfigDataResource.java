package com.prolabs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.prolabs.domain.ConfigData;
import com.prolabs.service.ConfigDataService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ConfigData.
 */
@RestController
@RequestMapping("/api")
public class ConfigDataResource {

    private final Logger log = LoggerFactory.getLogger(ConfigDataResource.class);

    private static final String ENTITY_NAME = "configData";

    private final ConfigDataService configDataService;

    public ConfigDataResource(ConfigDataService configDataService) {
        this.configDataService = configDataService;
    }

    /**
     * POST  /config-data : Create a new configData.
     *
     * @param configData the configData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configData, or with status 400 (Bad Request) if the configData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/config-data")
    @Timed
    public ResponseEntity<ConfigData> createConfigData(@Valid @RequestBody ConfigData configData) throws URISyntaxException {
        log.debug("REST request to save ConfigData : {}", configData);
        if (configData.getId() != null) {
            throw new BadRequestAlertException("A new configData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigData result = configDataService.save(configData);
        return ResponseEntity.created(new URI("/api/config-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /config-data : Updates an existing configData.
     *
     * @param configData the configData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configData,
     * or with status 400 (Bad Request) if the configData is not valid,
     * or with status 500 (Internal Server Error) if the configData couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/config-data")
    @Timed
    public ResponseEntity<ConfigData> updateConfigData(@Valid @RequestBody ConfigData configData) throws URISyntaxException {
        log.debug("REST request to update ConfigData : {}", configData);
        if (configData.getId() == null) {
            return createConfigData(configData);
        }
        ConfigData result = configDataService.save(configData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, configData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /config-data : get all the configData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of configData in body
     */
    @GetMapping("/config-data")
    @Timed
    public ResponseEntity<List<ConfigData>> getAllConfigData(Pageable pageable) {
        log.debug("REST request to get a page of ConfigData");
        Page<ConfigData> page = configDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/config-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /config-data/:id : get the "id" configData.
     *
     * @param id the id of the configData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configData, or with status 404 (Not Found)
     */
    @GetMapping("/config-data/{id}")
    @Timed
    public ResponseEntity<ConfigData> getConfigData(@PathVariable Long id) {
        log.debug("REST request to get ConfigData : {}", id);
        ConfigData configData = configDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(configData));
    }

    /**
     * DELETE  /config-data/:id : delete the "id" configData.
     *
     * @param id the id of the configData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/config-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfigData(@PathVariable Long id) {
        log.debug("REST request to delete ConfigData : {}", id);
        configDataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/config-data?query=:query : search for the configData corresponding
     * to the query.
     *
     * @param query the query of the configData search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/config-data")
    @Timed
    public ResponseEntity<List<ConfigData>> searchConfigData(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ConfigData for query {}", query);
        Page<ConfigData> page = configDataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/config-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
