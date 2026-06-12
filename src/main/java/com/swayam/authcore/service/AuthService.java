package com.swayam.authcore.service;

import com.swayam.authcore.dto.RegisterRequest;
import com.swayam.authcore.dto.UserResponse;
import com.swayam.authcore.entity.Role;
import com.swayam.authcore.entity.User;
import com.swayam.authcore.exception.EmailAlreadyExistsException;
import com.swayam.authcore.exception.UsernameAlreadyExistsException;
import com.swayam.authcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);
        log.info("Registered new user: id={}, username={}", saved.getId(), saved.getUsername());

        return UserResponse.from(saved);
    }
}
