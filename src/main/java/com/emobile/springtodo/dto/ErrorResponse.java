package com.emobile.springtodo.dto;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ErrorResponse(HttpStatus status, String message, Instant timestamp) {
}
