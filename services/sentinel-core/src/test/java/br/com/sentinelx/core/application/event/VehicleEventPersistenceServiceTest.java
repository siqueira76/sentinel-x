package br.com.sentinelx.core.application.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sentinelx.core.domain.agent.Agent;
import br.com.sentinelx.core.domain.agent.AgentStatus;
import br.com.sentinelx.core.domain.camera.Camera;
import br.com.sentinelx.core.domain.camera.CameraStatus;
import br.com.sentinelx.core.domain.event.EventDirection;
import br.com.sentinelx.core.domain.event.EventType;
import br.com.sentinelx.core.domain.event.VehicleEvent;
import br.com.sentinelx.core.infrastructure.persistence.AgentRepository;
import br.com.sentinelx.core.infrastructure.persistence.CameraRepository;
import br.com.sentinelx.core.infrastructure.persistence.VehicleEventRepository;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VehicleEventPersistenceServiceTest {

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private CameraRepository cameraRepository;

    @Mock
    private VehicleEventRepository vehicleEventRepository;

    @InjectMocks
    private VehicleEventPersistenceService service;

    private RegisterVehicleEventCommand command;

    @BeforeEach
    void setUp() {
        command = new RegisterVehicleEventCommand(
                "agent-1",
                AgentStatus.ACTIVE,
                "credential-ref",
                "{\"version\":1}",
                "camera-1",
                "Gate 01",
                "North lane",
                CameraStatus.ACTIVE,
                "{\"fps\":30}",
                "event-1",
                "idem-1",
                OffsetDateTime.parse("2026-03-15T10:15:30Z"),
                "ABC1D23",
                new BigDecimal("0.9810"),
                EventType.PLATE_READ,
                EventDirection.ENTRY,
                new RegisterVehicleEventCommand.EvidenceReferenceCommand(
                        "evidence/key.jpg",
                        "sha256:abc",
                        "image/jpeg",
                        12345L
                ),
                "{\"source\":\"test\"}"
        );
    }

    @Test
    void shouldReturnDuplicateWhenDeduplicationKeyAlreadyExists() {
        VehicleEvent existing = new VehicleEvent(
                new Agent("agent-1", AgentStatus.ACTIVE, null, null),
                new Camera(new Agent("agent-1", AgentStatus.ACTIVE, null, null), "camera-1", null, null, CameraStatus.ACTIVE, null),
                "event-1",
                "IDEMPOTENCY:idem-1",
                "idem-1",
                command.occurredAt(),
                command.plateNumber(),
                command.confidence(),
                command.eventType(),
                command.direction(),
                null,
                null
        );
        setField(existing, "id", UUID.fromString("11111111-1111-1111-1111-111111111111"));

        when(vehicleEventRepository.findByDeduplicationKey("IDEMPOTENCY:idem-1"))
                .thenReturn(Optional.of(existing));

        RegisterVehicleEventResult result = service.register(command);

        assertTrue(result.duplicate());
        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), result.id());
        verify(agentRepository, never()).save(any());
        verify(cameraRepository, never()).save(any());
    }

    @Test
    void shouldPersistNewAgentCameraAndEventWhenIdempotencyKeyIsNew() {
        when(vehicleEventRepository.findByDeduplicationKey("IDEMPOTENCY:idem-1"))
                .thenReturn(Optional.empty());
        when(agentRepository.findByExternalId("agent-1")).thenReturn(Optional.empty());
        when(agentRepository.save(any(Agent.class))).thenAnswer(invocation -> {
            Agent agent = invocation.getArgument(0);
            setField(agent, "id", 10L);
            return agent;
        });
        when(cameraRepository.findByAgentIdAndExternalId(10L, "camera-1")).thenReturn(Optional.empty());
        when(cameraRepository.save(any(Camera.class))).thenAnswer(invocation -> {
            Camera camera = invocation.getArgument(0);
            setField(camera, "id", 20L);
            return camera;
        });
        when(vehicleEventRepository.save(any(VehicleEvent.class))).thenAnswer(invocation -> {
            VehicleEvent event = invocation.getArgument(0);
            setField(event, "id", UUID.fromString("22222222-2222-2222-2222-222222222222"));
            return event;
        });

        RegisterVehicleEventResult result = service.register(command);

        assertFalse(result.duplicate());
        assertEquals(UUID.fromString("22222222-2222-2222-2222-222222222222"), result.id());
        verify(agentRepository).save(any(Agent.class));
        verify(cameraRepository).save(any(Camera.class));
        verify(vehicleEventRepository).save(any(VehicleEvent.class));
    }

    @Test
    void shouldFallbackToSourceCompositeWhenIdempotencyKeyIsMissing() {
        RegisterVehicleEventCommand withoutIdempotency = new RegisterVehicleEventCommand(
                command.agentId(),
                command.agentStatus(),
                command.agentCredentialReference(),
                command.agentMetadataJson(),
                command.cameraId(),
                command.cameraDisplayName(),
                command.cameraLocation(),
                command.cameraStatus(),
                command.cameraMetadataJson(),
                command.eventId(),
                null,
                command.occurredAt(),
                command.plateNumber(),
                command.confidence(),
                command.eventType(),
                command.direction(),
                command.evidence(),
                command.metadataJson()
        );

        assertEquals("SOURCE:agent-1:event-1", VehicleEventPersistenceService.deduplicationKeyFor(withoutIdempotency));
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