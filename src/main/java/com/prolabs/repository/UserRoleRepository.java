package com.prolabs.repository;

import com.prolabs.domain.UserRole;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the UserRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
   /* @Query("select distinct user_role from UserRole user_role left join fetch user_role.roles")
    List<UserRole> findAllWithEagerRelationships();

    @Query("select user_role from UserRole user_role left join fetch user_role.roles where user_role.id =:id")
    UserRole findOneWithEagerRelationships(@Param("id") Long id);*/

}
