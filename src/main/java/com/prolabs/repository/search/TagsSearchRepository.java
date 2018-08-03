package com.prolabs.repository.search;

import com.prolabs.domain.Tags;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tags entity.
 */
public interface TagsSearchRepository extends ElasticsearchRepository<Tags, Long> {
}
