package com.tyd.user.module.service;

import com.tyd.user.module.exception.UserNotFoundException;
import com.tyd.user.module.model.ApplicationUser;
import com.tyd.user.module.model.User;
import com.tyd.user.module.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailService implements UserDetailsService {

    public final UserRepository userRepository;

    public AppUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NotNull String emailOrMobile) throws UsernameNotFoundException {
        Optional<User> userOptional = emailOrMobile.contains("@") ? userRepository.findByUserEmail(emailOrMobile) : userRepository.findByUserMobile(emailOrMobile);
        return userOptional
                .map(ApplicationUser::new)
                .orElseThrow(() -> new UserNotFoundException("Email Or Mobile number provided not found"));
    }
}
