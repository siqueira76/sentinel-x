package br.com.sentinelx.core.api.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.sentinelx.core.application.event.VehicleEventDetails;
import br.com.sentinelx.core.application.event.VehicleEventNotFoundException;
import br.com.sentinelx.core.application.event.VehicleEventQueryService;
import br.com.sentinelx.core.application.event.VehicleEventSearchQuery;
import br.com.sentinelx.core.application.event.VehicleEventPersistenceService;
import br.com.sentinelx.core.application.event.VehicleEventSummary;
import br.com.sentinelx.core.domain.agent.AgentStatus;
import br.com.sentinelx.core.domain.camera.CameraStatus;
import br.com.sentinelx.core.domain.event.EventDirection;
import br.com.sentinelx.core.domain.event.EventType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration",
        "sentinel.security.agent.api-key=test-agent-key",
        "sentinel.security.admin.username=test-admin",
        "sentinel.security.admin.password=test-admin-password"
})
@AutoConfigureMockMvc
class VehicleEventQueryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleEventQueryService vehicleEventQueryService;

    @MockBean
    private VehicleEventPersistenceService vehicleEventPersistenceService;

    @Test
    void shouldReturnPaginatedEventsForAuthenticatedAdmin() throws Exception {
        VehicleEventSummary summary = new VehicleEventSummary(
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                "event-1",
                "agent-1",
                "camera-1",
                "Gate 01",
                OffsetDateTime.parse("2026-03-15T10:15:30Z"),
                "ABC1D23",
                new BigDecimal("0.9810"),
                EventType.PLATE_READ,
                EventDirection.ENTRY,
                true
        );
        when(vehicleEventQueryService.search(any()))
                .thenReturn(new PageImpl<>(List.of(summary), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/v1/events")
                        .with(httpBasic("test-admin", "test-admin-password"))
                        .param("from", "2026-03-15T00:00:00Z")
                        .param("to", "2026-03-16T00:00:00Z")
                        .param("agentId", "agent-1")
                        .param("cameraId", "camera-1")
                        .param("plateNumber", "abc1d23")
                        .param("eventType", "PLATE_READ")
                        .param("hasEvidence", "true")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value("44444444-4444-4444-4444-444444444444"))
                .andExpect(jsonPath("$.items[0].eventId").value("event-1"))
                .andExpect(jsonPath("$.items[0].agentId").value("agent-1"))
                .andExpect(jsonPath("$.items[0].cameraId").value("camera-1"))
                .andExpect(jsonPath("$.items[0].hasEvidence").value(true))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.size").value(20))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1));

        ArgumentCaptor<VehicleEventSearchQuery> queryCaptor = ArgumentCaptor.forClass(VehicleEventSearchQuery.class);
        verify(vehicleEventQueryService).search(queryCaptor.capture());
        assertEquals("agent-1", queryCaptor.getValue().agentId());
        assertEquals("camera-1", queryCaptor.getValue().cameraId());
        assertEquals("ABC1D23", queryCaptor.getValue().plateNumber());
        assertEquals(EventType.PLATE_READ, queryCaptor.getValue().eventType());
        assertEquals(Boolean.TRUE, queryCaptor.getValue().hasEvidence());
    }

    @Test
    void shouldRejectAnonymousAdminQuery() throws Exception {
        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.path").value("/api/v1/events"));

        verifyNoInteractions(vehicleEventQueryService);
    }

    @Test
    void shouldRejectInvalidPaginationParameters() throws Exception {
        mockMvc.perform(get("/api/v1/events")
                        .with(httpBasic("test-admin", "test-admin-password"))
                        .param("page", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("page must be greater than or equal to 0."));

        verifyNoInteractions(vehicleEventQueryService);
    }

    @Test
    void shouldRejectInvalidEventTypeParameter() throws Exception {
        mockMvc.perform(get("/api/v1/events")
                        .with(httpBasic("test-admin", "test-admin-password"))
                        .param("eventType", "INVALID_TYPE"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid value for parameter 'eventType'."));

        verifyNoInteractions(vehicleEventQueryService);
    }

    @Test
    void shouldReturnVehicleEventDetailsForAuthenticatedAdmin() throws Exception {
        UUID eventId = UUID.fromString("77777777-7777-7777-7777-777777777777");
        when(vehicleEventQueryService.getById(eventId)).thenReturn(new VehicleEventDetails(
                eventId,
                "event-1",
                "agent-1",
                AgentStatus.ACTIVE,
                "credential-ref",
                "{\"version\":1}",
                "camera-1",
                "Gate 01",
                "North lane",
                CameraStatus.ACTIVE,
                "{\"fps\":30}",
                "idem-1",
                OffsetDateTime.parse("2026-03-15T10:15:30Z"),
                OffsetDateTime.parse("2026-03-15T10:16:00Z"),
                "ABC1D23",
                new BigDecimal("0.9810"),
                EventType.PLATE_READ,
                EventDirection.ENTRY,
                new VehicleEventDetails.EvidenceReferenceDetails("evidence/key.jpg", "sha256:abc", "image/jpeg", 12345L),
                "{\"source\":\"integration-test\"}"
        ));

        mockMvc.perform(get("/api/v1/events/{id}", eventId)
                        .with(httpBasic("test-admin", "test-admin-password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId.toString()))
                .andExpect(jsonPath("$.eventId").value("event-1"))
                .andExpect(jsonPath("$.agent.id").value("agent-1"))
                .andExpect(jsonPath("$.agent.status").value("ACTIVE"))
                .andExpect(jsonPath("$.agent.credentialReference").value("credential-ref"))
                .andExpect(jsonPath("$.agent.metadata.version").value(1))
                .andExpect(jsonPath("$.camera.id").value("camera-1"))
                .andExpect(jsonPath("$.camera.displayName").value("Gate 01"))
                .andExpect(jsonPath("$.camera.location").value("North lane"))
                .andExpect(jsonPath("$.camera.status").value("ACTIVE"))
                .andExpect(jsonPath("$.camera.metadata.fps").value(30))
                .andExpect(jsonPath("$.idempotencyKey").value("idem-1"))
                .andExpect(jsonPath("$.evidence.key").value("evidence/key.jpg"))
                .andExpect(jsonPath("$.metadata.source").value("integration-test"));
    }

    @Test
    void shouldReturnNotFoundWhenVehicleEventDoesNotExist() throws Exception {
        UUID eventId = UUID.fromString("88888888-8888-8888-8888-888888888888");
        when(vehicleEventQueryService.getById(eventId)).thenThrow(new VehicleEventNotFoundException(eventId));

        mockMvc.perform(get("/api/v1/events/{id}", eventId)
                        .with(httpBasic("test-admin", "test-admin-password")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/api/v1/events/" + eventId))
                .andExpect(jsonPath("$.message").value("Vehicle event '" + eventId + "' was not found."));
    }
}