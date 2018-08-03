package com.prolabs.repository;

import com.prolabs.domain.RoleMst;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RoleMst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleMstRepository extends JpaRepository<RoleMst, Long> {

}
