package com.prolabs.repository;

import com.prolabs.domain.PermissionMst;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PermissionMst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionMstRepository extends JpaRepository<PermissionMst, Long> {

}
