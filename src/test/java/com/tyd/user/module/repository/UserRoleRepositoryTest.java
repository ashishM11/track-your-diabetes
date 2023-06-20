package com.tyd.user.module.repository;

import com.tyd.user.module.model.UserRole;
import com.tyd.user.module.model.UserRolePrivilege;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
@DataJpaTest
class UserRoleRepositoryTest {

    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserRolePrivilegeRepository userRolePrivilegeRepository;
    private UserRole userRole;
    private UserRolePrivilege userRolePrivilege;

    @BeforeEach
    void setUp() {
        userRolePrivilege = new UserRolePrivilege();
        userRolePrivilege.setUserRolePrivilegeName("READ_PRIVILEGE");
        userRolePrivilegeRepository.save(userRolePrivilege);

        userRole = new UserRole();
        userRole.setUserRoleName("USER_ROLE");
        userRole.setUserRolePrivileges(Collections.singletonList(userRolePrivilege));
        userRoleRepository.save(userRole);
    }

    @AfterEach
    void tearDown() {
        userRole = null;
        userRolePrivilege = null;
        userRoleRepository.deleteAll();
    }

    @Test
    void findByUserRoleNameSuccess() {
        Optional<UserRole> optionalUserRole = Optional.ofNullable(userRoleRepository.findByUserRoleName("USER_ROLE"));
        UserRole testUserRole = optionalUserRole.orElse(null);
        assertNotNull(testUserRole);
        assertThat(testUserRole.getUserRoleName()).isEqualTo(userRole.getUserRoleName());
    }

    @Test
    void findByUserRoleNameFailure() {
        Optional<UserRole> optionalUserRole = Optional.ofNullable(userRoleRepository.findByUserRoleName("USER_ROLE_1"));
        UserRole testUserRole = optionalUserRole.orElse(null);
        assertNull(testUserRole);
    }
}