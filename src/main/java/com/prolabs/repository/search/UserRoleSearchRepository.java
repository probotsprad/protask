package com.prolabs.repository.search;

import com.prolabs.domain.UserRole;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserRole entity.
 */
public interface UserRoleSearchRepository extends ElasticsearchRepository<UserRole, Long> {
}
