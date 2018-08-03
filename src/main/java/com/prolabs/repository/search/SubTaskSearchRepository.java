package com.prolabs.repository.search;

import com.prolabs.domain.SubTask;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SubTask entity.
 */
public interface SubTaskSearchRepository extends ElasticsearchRepository<SubTask, Long> {
}
