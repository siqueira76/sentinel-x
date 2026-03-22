package br.com.sentinelx.core.api.exception;

import br.com.sentinelx.core.application.event.VehicleEventNotFoundException;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        this.mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldReturnValidationErrorPayload() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.path").value("/test/validation"))
                .andExpect(jsonPath("$.details[0].field").value("name"));
    }

    @Test
    void shouldReturnMalformedRequestPayload() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .contentType(APPLICATION_JSON)
                        .content("{invalid-json}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MALFORMED_REQUEST"))
                .andExpect(jsonPath("$.path").value("/test/validation"));
    }

    @Test
    void shouldReturnInvalidRequestPayload() throws Exception {
        mockMvc.perform(get("/test/illegal"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid test request."));
    }

    @Test
    void shouldReturnInvalidRequestPayloadForTypeMismatch() throws Exception {
        mockMvc.perform(get("/test/type-mismatch")
                        .param("size", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid value for parameter 'size'."));
    }

    @Test
    void shouldReturnNotFoundPayload() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Vehicle event '99999999-9999-9999-9999-999999999999' was not found."))
                .andExpect(jsonPath("$.path").value("/test/not-found"));
    }

    @Test
    void shouldReturnInternalErrorPayload() throws Exception {
        mockMvc.perform(get("/test/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.path").value("/test/unexpected"));
    }

    @RestController
    static class TestController {

        @PostMapping("/test/validation")
        ResponseEntity<Void> validate(@Valid @RequestBody TestRequest request) {
            return ResponseEntity.ok().build();
        }

        @GetMapping("/test/illegal")
        ResponseEntity<Void> illegal() {
            throw new IllegalArgumentException("Invalid test request.");
        }

        @GetMapping("/test/unexpected")
        ResponseEntity<Void> unexpected() {
            throw new RuntimeException("Boom");
        }

        @GetMapping("/test/type-mismatch")
        ResponseEntity<Void> typeMismatch(@RequestParam Integer size) {
            return ResponseEntity.ok().build();
        }

        @GetMapping("/test/not-found")
        ResponseEntity<Void> notFound() {
            throw new VehicleEventNotFoundException(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        }
    }

    record TestRequest(@NotBlank(message = "must not be blank") String name) {
    }
}