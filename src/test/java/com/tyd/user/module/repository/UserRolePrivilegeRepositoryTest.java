package com.tyd.user.module.repository;

import com.tyd.user.module.model.UserRole;
import com.tyd.user.module.model.UserRolePrivilege;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class UserRolePrivilegeRepositoryTest {

    @Autowired
    private UserRolePrivilegeRepository userRolePrivilegeRepository;
    private UserRolePrivilege userRolePrivilege;

    @BeforeEach
    void setUp() {
        userRolePrivilege = new UserRolePrivilege();
        userRolePrivilege.setUserRolePrivilegeName("READ_PRIVILEGE");
        userRolePrivilegeRepository.save(userRolePrivilege);
    }

    @AfterEach
    void tearDown() {
        userRolePrivilege = null;
        userRolePrivilegeRepository.deleteAll();
    }

    @Test
    void findByUserRolePrivilegeNameSuccess() {
        Optional<UserRolePrivilege> optionalUserRolePrivilege = Optional.ofNullable(userRolePrivilegeRepository.findByUserRolePrivilegeName("READ_PRIVILEGE"));
        UserRolePrivilege testUserRolePrivilege = optionalUserRolePrivilege.orElse(null);
        assertNotNull(testUserRolePrivilege);
        assertThat(testUserRolePrivilege.getUserRolePrivilegeName()).isEqualTo(userRolePrivilege.getUserRolePrivilegeName());
    }

    @Test
    void findByUserRolePrivilegeNameFailure() {
        Optional<UserRolePrivilege> optionalUserRolePrivilege = Optional.ofNullable(userRolePrivilegeRepository.findByUserRolePrivilegeName("READ_PRIVILEGE_1"));
        UserRolePrivilege testUserRolePrivilege = optionalUserRolePrivilege.orElse(null);
        assertNull(testUserRolePrivilege);
    }
}