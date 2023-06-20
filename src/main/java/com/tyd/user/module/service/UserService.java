package com.tyd.user.module.service;

import com.tyd.user.module.dto.UserRequestDTO;
import com.tyd.user.module.dto.UserResponseDTO;
import com.tyd.user.module.dto.UserSignInRequestDTO;
import com.tyd.user.module.mapper.UserMapper;
import com.tyd.user.module.model.User;
import com.tyd.user.module.model.UserRole;
import com.tyd.user.module.repository.UserRepository;
import com.tyd.user.module.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
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


    @Transactional
    public UserResponseDTO createUser(@NonNull UserRequestDTO requestDTO) {
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
        return UserMapper.INSTANCE.fromUserModelToResponseDTO(userRepository.save(user));
    }

    @Transactional
    public UserResponseDTO findUserByItsId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElse(null);
        if(Objects.nonNull(user)){
            return UserMapper.INSTANCE.fromUserModelToResponseDTO(user);
        }
        return  null;
    }

    public String authenticate(UserSignInRequestDTO userSignInRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSignInRequestDTO.userEmailOrMobile(),userSignInRequestDTO.userPassword()));
        UserDetails userDetails = appUserDetailService.loadUserByUsername(userSignInRequestDTO.userEmailOrMobile());
        return jwtService.generateToken(userDetails);
    }

    public Set<UserResponseDTO> findAllUsers() {
        return UserMapper.INSTANCE.fromUserModelsToUserResponseDTOs(new HashSet<>(userRepository.findAll()));
    }

    public boolean forgetPassword(String userEmailOrMobile) {
        return Objects.nonNull(appUserDetailService.loadUserByUsername(userEmailOrMobile));
    }

    public UserResponseDTO getUserByEmailOrMobile(String userEmailOrMobile) {
        return null;
    }
}
