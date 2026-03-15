package br.com.sentinelx.core.infrastructure.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration",
        "sentinel.security.agent.api-key=test-agent-key",
        "sentinel.security.admin.username=test-admin",
        "sentinel.security.admin.password=test-admin-password"
})
@AutoConfigureMockMvc
@Import(SecurityConfigIntegrationTest.TestSecurityApiController.class)
class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeHealthWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectAnonymousAdminRequest() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.path").value("/api/test/admin"));
    }

    @Test
    void shouldAllowAdminRequestWithBasicAuthentication() throws Exception {
        mockMvc.perform(get("/api/test/admin")
                        .with(httpBasic("test-admin", "test-admin-password")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectAgentIngestionWithoutApiKey() throws Exception {
        mockMvc.perform(post("/api/v1/agent-events/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.path").value("/api/v1/agent-events/test"));
    }

    @Test
    void shouldRejectAgentIngestionWithInvalidApiKey() throws Exception {
        mockMvc.perform(post("/api/v1/agent-events/test")
                        .header("X-Agent-Api-Key", "wrong-key"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));
    }

    @Test
    void shouldAllowAgentIngestionWithValidApiKey() throws Exception {
        mockMvc.perform(post("/api/v1/agent-events/test")
                        .header("X-Agent-Api-Key", "test-agent-key"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectAdminCredentialsOnAgentEndpoint() throws Exception {
        mockMvc.perform(post("/api/v1/agent-events/test")
                        .with(httpBasic("test-admin", "test-admin-password")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));
    }

    @RestController
    static class TestSecurityApiController {

        @GetMapping("/api/test/admin")
        ResponseEntity<String> admin() {
            return ResponseEntity.ok("admin-ok");
        }

        @PostMapping("/api/v1/agent-events/test")
        ResponseEntity<String> agentEvent() {
            return ResponseEntity.ok("agent-ok");
        }
    }
}