package com.prolabs.repository;

import com.prolabs.domain.Context;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Context entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {

}
