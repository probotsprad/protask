package com.prolabs.repository;

import com.prolabs.domain.UserMst;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserMst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserMstRepository extends JpaRepository<UserMst, Long> {

}
