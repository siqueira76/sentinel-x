package br.com.sentinelx.core.api.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.sentinelx.core.application.event.RegisterVehicleEventCommand;
import br.com.sentinelx.core.application.event.RegisterVehicleEventResult;
import br.com.sentinelx.core.application.event.VehicleEventPersistenceService;
import br.com.sentinelx.core.application.event.VehicleEventQueryService;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration",
        "sentinel.security.agent.api-key=test-agent-key",
        "sentinel.security.admin.username=test-admin",
        "sentinel.security.admin.password=test-admin-password"
})
@AutoConfigureMockMvc
class AgentEventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleEventPersistenceService vehicleEventPersistenceService;

    @MockBean
    private VehicleEventQueryService vehicleEventQueryService;

    @Test
    void shouldRegisterAgentEventAndReturnAcceptedResponse() throws Exception {
        UUID createdId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        when(vehicleEventPersistenceService.register(any()))
                .thenReturn(new RegisterVehicleEventResult(createdId, false));

        mockMvc.perform(post("/api/v1/agent-events")
                        .header("X-Agent-Api-Key", "test-agent-key")
                        .contentType(APPLICATION_JSON)
                        .content(validRequestJson()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(createdId.toString()))
                .andExpect(jsonPath("$.eventId").value("event-1"))
                .andExpect(jsonPath("$.status").value("ACCEPTED"))
                .andExpect(jsonPath("$.duplicate").value(false));

        ArgumentCaptor<RegisterVehicleEventCommand> commandCaptor = ArgumentCaptor.forClass(RegisterVehicleEventCommand.class);
        verify(vehicleEventPersistenceService).register(commandCaptor.capture());

        RegisterVehicleEventCommand command = commandCaptor.getValue();
        assertEquals("agent-1", command.agentId());
        assertEquals("camera-1", command.cameraId());
        assertEquals("event-1", command.eventId());
        assertEquals("idem-1", command.idempotencyKey());
        assertEquals("ABC1D23", command.plateNumber());
        assertEquals(new BigDecimal("0.9810"), command.confidence());
        assertNotNull(command.evidence());
        assertEquals("{\"source\":\"integration-test\"}", command.metadataJson());
    }

    @Test
    void shouldReturnDuplicateResponseWhenEventAlreadyExists() throws Exception {
        UUID existingId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        when(vehicleEventPersistenceService.register(any()))
                .thenReturn(new RegisterVehicleEventResult(existingId, true));

        mockMvc.perform(post("/api/v1/agent-events")
                        .header("X-Agent-Api-Key", "test-agent-key")
                        .contentType(APPLICATION_JSON)
                        .content(validRequestJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId.toString()))
                .andExpect(jsonPath("$.status").value("DUPLICATE"))
                .andExpect(jsonPath("$.duplicate").value(true));
    }

    @Test
    void shouldRejectUnauthorizedIngestionWithoutApiKey() throws Exception {
        mockMvc.perform(post("/api/v1/agent-events")
                        .contentType(APPLICATION_JSON)
                        .content(validRequestJson()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.path").value("/api/v1/agent-events"));

        verifyNoInteractions(vehicleEventPersistenceService);
    }

    @Test
    void shouldRejectInvalidRequestBody() throws Exception {
        mockMvc.perform(post("/api/v1/agent-events")
                        .header("X-Agent-Api-Key", "test-agent-key")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.path").value("/api/v1/agent-events"));

        verifyNoInteractions(vehicleEventPersistenceService);
    }

    private static String validRequestJson() {
        return """
                {
                  \"agentId\": \"agent-1\",
                  \"cameraId\": \"camera-1\",
                  \"eventId\": \"event-1\",
                  \"idempotencyKey\": \"idem-1\",
                  \"occurredAt\": \"2026-03-15T10:15:30Z\",
                  \"plateNumber\": \"ABC1D23\",
                  \"confidence\": 0.9810,
                  \"eventType\": \"PLATE_READ\",
                  \"direction\": \"ENTRY\",
                  \"cameraDisplayName\": \"Gate 01\",
                  \"cameraLocation\": \"North lane\",
                  \"evidence\": {
                    \"key\": \"evidence/key.jpg\",
                    \"hash\": \"sha256:abc\",
                    \"contentType\": \"image/jpeg\",
                    \"sizeBytes\": 12345
                  },
                  \"metadata\": {
                    \"source\": \"integration-test\"
                  }
                }
                """;
    }
}