package br.com.sentinelx.core.application.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sentinelx.core.domain.agent.Agent;
import br.com.sentinelx.core.domain.agent.AgentStatus;
import br.com.sentinelx.core.domain.camera.Camera;
import br.com.sentinelx.core.domain.camera.CameraStatus;
import br.com.sentinelx.core.domain.event.EvidenceReference;
import br.com.sentinelx.core.domain.event.EventDirection;
import br.com.sentinelx.core.domain.event.EventType;
import br.com.sentinelx.core.domain.event.VehicleEvent;
import br.com.sentinelx.core.infrastructure.persistence.VehicleEventRepository;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class VehicleEventQueryServiceTest {

    @Mock
    private VehicleEventRepository vehicleEventRepository;

    @InjectMocks
    private VehicleEventQueryService service;

    @Test
    void shouldReturnMappedPageOfVehicleEvents() throws Exception {
        VehicleEventSearchQuery query = new VehicleEventSearchQuery(
                OffsetDateTime.parse("2026-03-15T00:00:00Z"),
                OffsetDateTime.parse("2026-03-16T00:00:00Z"),
                "agent-1",
                "camera-1",
                "abc1d23",
                EventType.PLATE_READ,
                true,
                0,
                20
        );

        Agent agent = new Agent("agent-1", AgentStatus.ACTIVE, "credential-ref", null);
        Camera camera = new Camera(agent, "camera-1", "Gate 01", "North lane", CameraStatus.ACTIVE, null);
        VehicleEvent event = new VehicleEvent(
                agent,
                camera,
                "event-1",
                "SOURCE:agent-1:event-1",
                "idem-1",
                OffsetDateTime.parse("2026-03-15T10:15:30Z"),
                "ABC1D23",
                new BigDecimal("0.9810"),
                EventType.PLATE_READ,
                EventDirection.ENTRY,
                new EvidenceReference("evidence/key.jpg", "sha256:abc", "image/jpeg", 12345L),
                "{\"source\":\"test\"}"
        );
        setField(event, "id", UUID.fromString("33333333-3333-3333-3333-333333333333"));

        when(vehicleEventRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(event), Pageable.ofSize(20), 1));

        Page<VehicleEventSummary> result = service.search(query);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("ABC1D23", result.getContent().getFirst().plateNumber());
        assertEquals("agent-1", result.getContent().getFirst().agentId());
        assertEquals("camera-1", result.getContent().getFirst().cameraId());
        assertTrue(result.getContent().getFirst().hasEvidence());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(vehicleEventRepository).findAll(any(Specification.class), pageableCaptor.capture());
        assertEquals(0, pageableCaptor.getValue().getPageNumber());
        assertEquals(20, pageableCaptor.getValue().getPageSize());
        assertTrue(pageableCaptor.getValue().getSort().getOrderFor("occurredAt").isDescending());
        assertTrue(pageableCaptor.getValue().getSort().getOrderFor("createdAt").isDescending());
    }

    @Test
    void shouldNormalizeAndValidateSearchQuery() {
        VehicleEventSearchQuery query = new VehicleEventSearchQuery(
                null,
                null,
                " agent-1 ",
                " ",
                "abc1d23",
                null,
                false,
                1,
                10
        );

        assertEquals("agent-1", query.agentId());
        assertEquals("ABC1D23", query.plateNumber());
        assertEquals(null, query.cameraId());
        assertFalse(query.hasEvidence());
    }

    @Test
    void shouldReturnDetailedVehicleEventById() {
        UUID eventId = UUID.fromString("55555555-5555-5555-5555-555555555555");
        Agent agent = new Agent("agent-1", AgentStatus.ACTIVE, "credential-ref", "{\"version\":1}");
        Camera camera = new Camera(agent, "camera-1", "Gate 01", "North lane", CameraStatus.ACTIVE, "{\"fps\":30}");
        VehicleEvent event = new VehicleEvent(
                agent,
                camera,
                "event-1",
                "SOURCE:agent-1:event-1",
                "idem-1",
                OffsetDateTime.parse("2026-03-15T10:15:30Z"),
                "ABC1D23",
                new BigDecimal("0.9810"),
                EventType.PLATE_READ,
                EventDirection.ENTRY,
                new EvidenceReference("evidence/key.jpg", "sha256:abc", "image/jpeg", 12345L),
                "{\"source\":\"test\"}"
        );
        setField(event, "id", eventId);
        setField(event, "createdAt", OffsetDateTime.parse("2026-03-15T10:16:00Z"));

        when(vehicleEventRepository.findById(eventId)).thenReturn(Optional.of(event));

        VehicleEventDetails result = service.getById(eventId);

        assertEquals(eventId, result.id());
        assertEquals("event-1", result.eventId());
        assertEquals("agent-1", result.agentId());
        assertEquals(AgentStatus.ACTIVE, result.agentStatus());
        assertEquals("camera-1", result.cameraId());
        assertEquals(CameraStatus.ACTIVE, result.cameraStatus());
        assertEquals("idem-1", result.idempotencyKey());
        assertEquals(OffsetDateTime.parse("2026-03-15T10:16:00Z"), result.createdAt());
        assertEquals("evidence/key.jpg", result.evidence().key());
    }

    @Test
    void shouldThrowWhenVehicleEventDoesNotExist() {
        UUID eventId = UUID.fromString("66666666-6666-6666-6666-666666666666");
        when(vehicleEventRepository.findById(eventId)).thenReturn(Optional.empty());

        VehicleEventNotFoundException exception = assertThrows(
                VehicleEventNotFoundException.class,
                () -> service.getById(eventId)
        );

        assertEquals("Vehicle event '" + eventId + "' was not found.", exception.getMessage());
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}