package dev.rhys.workouttracker.controller;

import dev.rhys.workouttracker.dto.AuthResponse;
import dev.rhys.workouttracker.dto.LoginRequest;
import dev.rhys.workouttracker.dto.SignupRequest;
import dev.rhys.workouttracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
    }


    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
