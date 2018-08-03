package com.prolabs.repository;

import com.prolabs.domain.Policies;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Policies entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoliciesRepository extends JpaRepository<Policies, Long> {

}
