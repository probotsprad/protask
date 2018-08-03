package com.prolabs.repository.search;

import com.prolabs.domain.RoleMst;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RoleMst entity.
 */
public interface RoleMstSearchRepository extends ElasticsearchRepository<RoleMst, Long> {
}
