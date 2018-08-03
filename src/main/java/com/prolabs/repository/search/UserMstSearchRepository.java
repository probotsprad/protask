package com.prolabs.repository.search;

import com.prolabs.domain.UserMst;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserMst entity.
 */
public interface UserMstSearchRepository extends ElasticsearchRepository<UserMst, Long> {
}
