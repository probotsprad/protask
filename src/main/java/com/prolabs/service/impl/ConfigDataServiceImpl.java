package com.prolabs.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.prolabs.service.ConfigDataService;
import com.prolabs.domain.ConfigData;
import com.prolabs.domain.UserMst;
import com.prolabs.repository.ConfigDataRepository;
import com.prolabs.repository.search.ConfigDataSearchRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ConfigData.
 */
@Service
@Transactional
public class ConfigDataServiceImpl implements ConfigDataService{

    private final Logger log = LoggerFactory.getLogger(ConfigDataServiceImpl.class);

    private final ConfigDataRepository configDataRepository;

    private final ConfigDataSearchRepository configDataSearchRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public ConfigDataServiceImpl(ConfigDataRepository configDataRepository, ConfigDataSearchRepository configDataSearchRepository) {
        this.configDataRepository = configDataRepository;
        this.configDataSearchRepository = configDataSearchRepository;
    }

    /**
     * Save a configData.
     *
     * @param configData the entity to save
     * @return the persisted entity
     */
    @Override
    public ConfigData save(ConfigData configData) {
        log.debug("Request to save ConfigData : {}", configData);
        ConfigData result = configDataRepository.save(configData);
        configDataSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the configData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigData> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigData");
        return configDataRepository.findAll(pageable);
    }

    /**
     * Get one configData by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ConfigData findOne(Long id) {
        log.debug("Request to get ConfigData : {}", id);
        return configDataRepository.findOne(id);
    }

    /**
     * Delete the configData by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigData : {}", id);
        configDataRepository.delete(id);
        configDataSearchRepository.delete(id);
    }

    /**
     * Search for the configData corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigData> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ConfigData for query {}", query);
        Page<ConfigData> result = configDataSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

	@Override
	public String getMediusAdminMenuJson(String key) {
		Query query = entityManager.createQuery(" from ConfigData cfg where cfg.key= :key");
		query.setParameter("key", key);
		ConfigData configData = (ConfigData) query.getSingleResult();
		if(configData != null)
			return configData.getValue();
		return null;
	}
	
	@Override
	public UserMst getUserByUserId(String userId) {
		Query query = entityManager.createQuery(" from UserMst user where user.userId= :userId");
		query.setParameter("userId", userId);
		return (UserMst) query.getSingleResult();
	}

}
