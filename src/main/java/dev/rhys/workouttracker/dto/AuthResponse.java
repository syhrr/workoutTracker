package dev.rhys.workouttracker.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthResponse(
    @NotBlank String token,
    @NotBlank long expiresIn
) {}
