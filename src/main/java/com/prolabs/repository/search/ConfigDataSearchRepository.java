package com.prolabs.repository.search;

import com.prolabs.domain.ConfigData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ConfigData entity.
 */
public interface ConfigDataSearchRepository extends ElasticsearchRepository<ConfigData, Long> {
}
