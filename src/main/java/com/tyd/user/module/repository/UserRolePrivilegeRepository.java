package com.tyd.user.module.repository;

import com.tyd.user.module.model.UserRolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolePrivilegeRepository extends JpaRepository<UserRolePrivilege, Long> {
    UserRolePrivilege findByUserRolePrivilegeName(String privilegeName);
}
