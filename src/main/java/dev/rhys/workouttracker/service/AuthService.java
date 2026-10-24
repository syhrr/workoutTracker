package dev.rhys.workouttracker.service;

import dev.rhys.workouttracker.config.JwtProperties;

import dev.rhys.workouttracker.dto.AuthResponse;
import dev.rhys.workouttracker.dto.LoginRequest;
import dev.rhys.workouttracker.dto.SignupRequest;
import dev.rhys.workouttracker.entity.User;
import dev.rhys.workouttracker.exception.EmailAlreadyExistsException;
import dev.rhys.workouttracker.exception.InvalidCredentialsException;
import dev.rhys.workouttracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtEncoder jwtEncoder,
                       JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
    }


    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException(request.email());
        } else {

            User user = new User();

            user.setEmail(request.email());
            user.setPasswordHash(passwordEncoder.encode(request.password()));
            userRepository.save(user);

        }
    }

    public AuthResponse login(LoginRequest request){
        // 1. check if user acc exists in db first
        User user = userRepository.findByEmail(request.email()).orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())){
            throw new InvalidCredentialsException();
        } else {

            Instant now = Instant.now();
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .subject(String.valueOf(user.getId()))
                    .issuedAt(now)
                    .expiresAt(now.plusMillis(jwtProperties.expirationMs()))
                    .build();

            JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
            String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

            return new AuthResponse(token, jwtProperties.expirationMs());
        }


    }
}
