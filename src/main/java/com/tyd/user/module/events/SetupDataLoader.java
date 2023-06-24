package com.tyd.user.module.events;

import com.tyd.user.module.model.Password;
import com.tyd.user.module.model.User;
import com.tyd.user.module.model.UserRole;
import com.tyd.user.module.model.UserRolePrivilege;
import com.tyd.user.module.repository.UserRepository;
import com.tyd.user.module.repository.UserRolePrivilegeRepository;
import com.tyd.user.module.repository.UserRoleRepository;
import lombok.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;
    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final UserRolePrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepository userRepository, UserRoleRepository roleRepository, UserRolePrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        if (alreadySetup) return;

        UserRolePrivilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        UserRolePrivilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<UserRolePrivilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", List.of(readPrivilege));

        UserRole adminRole = roleRepository.findByUserRoleName("ROLE_ADMIN");

        User user = new User();
        user.setUserFName("Test");
        user.setUserLName("Test");

        Password password = new Password();
        password.setEncryptedPassword(passwordEncoder.encode("test"));
        password.setPasswordCreationDT(LocalDate.now());
        user.setPassword(password);
        user.setUserEmail("admin@test.com");
        user.setUserMobile("1234567890");
        user.setUserRoles(Collections.singletonList(adminRole));
        user.setUserGender("O");
        user.setUserCreationDT(LocalDate.now());
        user.setUserAccountEnabled(true);
        user.setUserAccountNonLocked(true);
        user.setUserCredentialsNonExpired(true);
        user.setUserAccountNonExpired(true);
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    private void createRoleIfNotFound(String roleName, List<UserRolePrivilege> privileges) {
        UserRole role = roleRepository.findByUserRoleName(roleName);
        if (role == null) {
            role = new UserRole();
            role.setUserRoleName(roleName);
            role.setUserRolePrivileges(privileges);
            roleRepository.save(role);
        }
    }

    @Transactional
    private UserRolePrivilege createPrivilegeIfNotFound(String privilegeName) {

        UserRolePrivilege privilege = privilegeRepository.findByUserRolePrivilegeName(privilegeName);
        if (privilege == null) {
            privilege = new UserRolePrivilege();
            privilege.setUserRolePrivilegeName(privilegeName);
            privilegeRepository.saveAndFlush(privilege);
        }
        return privilege;
    }
}
