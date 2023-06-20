package com.tyd.user.module.repository;

import com.tyd.user.module.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    UserRole findByUserRoleName(String roleAdmin);
}
