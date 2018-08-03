package com.prolabs.repository.search;

import com.prolabs.domain.RolePermission;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RolePermission entity.
 */
public interface RolePermissionSearchRepository extends ElasticsearchRepository<RolePermission, Long> {
}
