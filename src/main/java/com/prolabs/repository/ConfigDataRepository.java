package com.prolabs.repository;

import com.prolabs.domain.ConfigData;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ConfigData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigDataRepository extends JpaRepository<ConfigData, Long> {

}
