package dev.rhys.workouttracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
* I shape of the JSON body for POST /api/auth/signup:
{ "email": "a@b.com", "password": "password123" }
* this is so that the raw password only exists here
* */

public record SignupRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8) String password
) {}
