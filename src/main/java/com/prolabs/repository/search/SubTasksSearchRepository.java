package com.prolabs.repository.search;

import com.prolabs.domain.SubTasks;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SubTasks entity.
 */
public interface SubTasksSearchRepository extends ElasticsearchRepository<SubTasks, Long> {
}
