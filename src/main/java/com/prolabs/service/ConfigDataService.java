package com.prolabs.service;

import com.prolabs.domain.ConfigData;
import com.prolabs.domain.UserMst;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ConfigData.
 */
public interface ConfigDataService {

    /**
     * Save a configData.
     *
     * @param configData the entity to save
     * @return the persisted entity
     */
    ConfigData save(ConfigData configData);

    /**
     * Get all the configData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConfigData> findAll(Pageable pageable);

    /**
     * Get the "id" configData.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ConfigData findOne(Long id);

    /**
     * Delete the "id" configData.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the configData corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConfigData> search(String query, Pageable pageable);
    
    public String getMediusAdminMenuJson(String key);

	UserMst getUserByUserId(String userId);
}
