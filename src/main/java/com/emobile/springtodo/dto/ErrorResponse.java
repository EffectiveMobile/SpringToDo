package com.emobile.springtodo.dto;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ErrorResponse(int code,HttpStatus status, String message, Instant timestamp) {
}
