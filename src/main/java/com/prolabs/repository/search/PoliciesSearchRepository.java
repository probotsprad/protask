package com.prolabs.repository.search;

import com.prolabs.domain.Policies;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Policies entity.
 */
public interface PoliciesSearchRepository extends ElasticsearchRepository<Policies, Long> {
}
