package dev.rhys.workouttracker.service;

import dev.rhys.workouttracker.config.JwtProperties;
import dev.rhys.workouttracker.dto.AuthResponse;
import dev.rhys.workouttracker.dto.LoginRequest;
import dev.rhys.workouttracker.dto.SignupRequest;
import dev.rhys.workouttracker.entity.User;
import dev.rhys.workouttracker.exception.EmailAlreadyExistsException;
import dev.rhys.workouttracker.exception.InvalidCredentialsException;
import dev.rhys.workouttracker.repository.UserRepository;

import org.apache.juli.logging.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtEncoder jwtEncoder;
    @Mock
    JwtProperties jwtProperties;

    @InjectMocks
    AuthService authService;

    @Test
    void signup_savesUserWithHashedPassword() {
        when(userRepository.existsByEmail("a@b.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("HASH");

        authService.signup(new SignupRequest("a@b.com", "password123"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("a@b.com", captor.getValue().getEmail());
        assertEquals("HASH", captor.getValue().getPasswordHash());
    }

    @Test
    void signup_neverStoresRawPassword() {
        when(userRepository.existsByEmail("a@b.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("HASH");

        authService.signup(new SignupRequest("a@b.com", "password123"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertNotEquals("password123", captor.getValue().getPasswordHash());
    }

    @Test
    void signup_duplicateEmailThrowsException(){
        when(userRepository.existsByEmail("a@b.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> authService.signup(new SignupRequest("a@b.com", "password123")));

        verify(userRepository, never()).save(any());
    }


    // /login


    @Test
    void login_unknownEmail_ThrowsInvalidCredentials(){
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new LoginRequest("a@b.com", "password123")));
    }

    @Test
    void login_wrongPassword_ThrowsInvalidCredentials(){
        User user = new User();
        user.setPasswordHash("HASH");
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "HASH")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new LoginRequest("a@b.com", "wrongpassword")));
    }

    @Test
    void login_successfulLogin_returnsTokenAndExpiry(){
        User user = new User();
        user.setPasswordHash("HASH");
        ReflectionTestUtils.setField(user, "id", 42L);
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "HASH")).thenReturn(true);
        when(jwtProperties.expirationMs()).thenReturn(3600000L);

        Jwt jwt = Jwt.withTokenValue("TOKEN").header("alg", "HS256").subject("42").build();
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        AuthResponse response = authService.login(new LoginRequest("a@b.com", "password123"));

        assertEquals("TOKEN", response.token());
        assertEquals(3600000L, response.expiresIn());

        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(jwtEncoder).encode(captor.capture());
        assertEquals("42", captor.getValue().getClaims().getSubject());
    }

}