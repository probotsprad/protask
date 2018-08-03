package com.prolabs.repository.search;

import com.prolabs.domain.PermissionMst;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PermissionMst entity.
 */
public interface PermissionMstSearchRepository extends ElasticsearchRepository<PermissionMst, Long> {
}
