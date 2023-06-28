package com.tyd.user.module.service;

import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.tyd.user.module.dto.ChangePasswordRequestDTO;
import com.tyd.user.module.dto.UserRequestDTO;
import com.tyd.user.module.dto.UserResponseDTO;
import com.tyd.user.module.dto.UserSignInRequestDTO;
import com.tyd.user.module.exception.UserNotFoundException;
import com.tyd.user.module.mapper.UserMapper;
import com.tyd.user.module.model.User;
import com.tyd.user.module.model.UserRole;
import com.tyd.user.module.repository.UserRepository;
import com.tyd.user.module.repository.UserRoleRepository;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AppUserDetailService appUserDetailService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    @Transactional
    public String createUser(@NonNull UserRequestDTO requestDTO) throws MessagingException {
        LocalDate currentDT = LocalDate.now();
        User user = UserMapper.INSTANCE.fromUserRequestDTO(requestDTO);
        user.setUserCreationDT(currentDT);
        user.getPassword().setPasswordCreationDT(currentDT);
        UserRole userRole = userRoleRepository.findByUserRoleName("ROLE_USER");
        user.setUserRoles(Set.of(userRole));
        user.setUserAccountEnabled(true);
        user.setUserAccountNonExpired(true);
        user.setUserAccountNonLocked(true);
        user.setUserCredentialsNonExpired(true);
        user.getPassword().setEncryptedPassword(passwordEncoder.encode(user.getPassword().getEncryptedPassword()));
        userRepository.save(user);
        mailService.sendEmailFromTemplate("manosoft.creation@gmail.com", user.getUserEmail());
        return "Thank You for Registration.";
    }

    @Transactional
    public UserResponseDTO findUserByItsId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElse(null);
        if (Objects.nonNull(user)) {
            return UserMapper.INSTANCE.fromUserModelToResponseDTO(user);
        }
        return null;
    }

    @Transactional
    public String authenticate(UserSignInRequestDTO userSignInRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSignInRequestDTO.userEmailOrMobile(), userSignInRequestDTO.userPassword()));
        UserDetails userDetails = appUserDetailService.loadUserByUsername(userSignInRequestDTO.userEmailOrMobile());
        return jwtService.generateToken(userDetails);
    }

    @Transactional
    public Set<UserResponseDTO> findAllUsers() {
        return UserMapper.INSTANCE.fromUserModelsToUserResponseDTOs(new HashSet<>(userRepository.findAll()));
    }

    @Transactional
    public boolean forgetPassword(String userEmailOrMobile) {
        return Objects.nonNull(appUserDetailService.loadUserByUsername(userEmailOrMobile));
    }

    public UserResponseDTO getUserByEmailOrMobile(String userEmailOrMobile) {
        return null;
    }

    @Transactional
    public String changePasswordRequest(ChangePasswordRequestDTO changePasswordRequestDTO) {
        String currentPassword, newPassword, confirmPassword;
        if (Objects.nonNull(changePasswordRequestDTO)) {
            Optional<User> byUserEmail = userRepository.findByUserEmail(changePasswordRequestDTO.userEmail().trim());
            User user = byUserEmail.orElseThrow(() -> new UserNotFoundException("Unable to find user."));
            currentPassword = changePasswordRequestDTO.oldPassword().trim();
            newPassword = changePasswordRequestDTO.newPassword().trim();
            confirmPassword = changePasswordRequestDTO.confirmPassword().trim();
            if (newPassword.equals(confirmPassword) && passwordEncoder.matches(currentPassword,user.getPassword().getEncryptedPassword())) {
                user.getPassword().setEncryptedPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return "Password changed successfully";
            }
            return "Old Password Not Matched";
        }
        return null;
    }
}
