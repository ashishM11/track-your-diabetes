package com.tyd.user.module.repository;

import com.tyd.user.module.model.Password;
import com.tyd.user.module.model.User;
import com.tyd.user.module.model.UserRole;
import com.tyd.user.module.model.UserRolePrivilege;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserRolePrivilegeRepository userRolePrivilegeRepository;
    private User user;
    private Password password;
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

        password = new Password();
        password.setEncryptedPassword("test");
        password.setPasswordCreationDT(LocalDate.now());

        user = new User();
        user.setUserCreationDT(LocalDate.now());
        user.setUserEmail("test@gmail.com");
        user.setUserFName("test");
        user.setUserLName("test");
        user.setUserMobile("1234567890");
        user.setUserGender("M");
        user.setUserDOB(LocalDate.now());
        user.setUserAccountEnabled(true);
        user.setUserAccountNonExpired(true);
        user.setUserAccountNonLocked(true);
        user.setUserCredentialsNonExpired(true);
        user.setUserRoles(Collections.singletonList(userRole));
        user.setPassword(password);
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        user = null;
        userRole = null;
        userRolePrivilege = null;
        password = null;
        userRepository.deleteAll();
    }

    @Test
    void findByUserEmailSuccess() {
        Optional<User> userOptional = userRepository.findByUserEmail("test@gmail.com");
        User testUser = userOptional.orElse(null);
        assertNotNull(testUser);
        assertThat(testUser.getUserEmail()).isEqualTo(user.getUserEmail());
    }

    @Test
    void findByUserMobileSuccess() {
        Optional<User> userOptional = userRepository.findByUserMobile("1234567890");
        User testUser = userOptional.orElse(null);
        assertNotNull(testUser);
        assertThat(testUser.getUserEmail()).isEqualTo(user.getUserEmail());
    }

    @Test
    void findByUserEmailFailure() {
        Optional<User> userOptional = userRepository.findByUserEmail("test1@gmail.com");
        User testUser = userOptional.orElse(null);
        assertNull(testUser);
    }

    @Test
    void findByUserMobileFailure() {
        Optional<User> userOptional = userRepository.findByUserMobile("");
        User testUser = userOptional.orElse(null);
        assertNull(testUser);
    }
}