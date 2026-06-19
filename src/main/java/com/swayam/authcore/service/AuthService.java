package com.swayam.authcore.service;

import com.swayam.authcore.dto.AuthResponse;
import com.swayam.authcore.dto.LoginRequest;
import com.swayam.authcore.dto.RegisterRequest;
import com.swayam.authcore.dto.UserResponse;
import com.swayam.authcore.entity.Role;
import com.swayam.authcore.entity.User;
import com.swayam.authcore.exception.EmailAlreadyExistsException;
import com.swayam.authcore.exception.InvalidCredentialsException;
import com.swayam.authcore.exception.UsernameAlreadyExistsException;
import com.swayam.authcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.swayam.authcore.dto.RefreshRequest;
import com.swayam.authcore.exception.InvalidTokenException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtService.generateAccessToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        log.info("User logged in: username={}", user.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }
    @Transactional(readOnly = true)
    public AuthResponse refresh(RefreshRequest request) {
        String token = request.getRefreshToken();
        if (!jwtService.isTokenValid(token)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }
        String type = jwtService.extractTokenType(token);
        if (!"refresh".equals(type)) {
            throw new InvalidTokenException("Provided token is not a refresh token");
        }
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidTokenException("User no longer exists"));

        String newAccess = jwtService.generateAccessToken(user.getUsername(), user.getRole().name());
        String newRefresh = jwtService.generateRefreshToken(user.getUsername());

        return AuthResponse.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .tokenType("Bearer")
                .build();
    }
}
