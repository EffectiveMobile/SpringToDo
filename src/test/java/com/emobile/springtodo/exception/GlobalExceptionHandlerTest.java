package com.emobile.springtodo.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void testHandleTodoNotFoundException() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testHandleHttpMessageNotReadableException() throws Exception {
        mockMvc.perform(get("/test/bad-request"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleInvalidDataException() throws Exception {
        mockMvc.perform(get("/test/invalid-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleGenericException() throws Exception {
        mockMvc.perform(get("/test/generic-error"))
                .andExpect(status().isInternalServerError());
    }

    @RestController
    @RequestMapping("/test")
    private static class TestController {

        @GetMapping("/not-found")
        public void throwTodoNotFoundException() throws TodoNotFoundException {
            throw new TodoNotFoundException(1L);
        }

        @GetMapping("/bad-request")
        public void throwHttpMessageNotReadableException() throws HttpMessageNotReadableException {
            throw new HttpMessageNotReadableException("Invalid JSON", new InvalidFormatException(null, "Invalid value", null, null));
        }

        @GetMapping("/invalid-data")
        public void throwInvalidDataException() {
            throw new InvalidDataException("Invalid data provided");
        }

        @GetMapping("/generic-error")
        public void throwGenericException() {
            throw new RuntimeException("Unexpected error");
        }
    }
}