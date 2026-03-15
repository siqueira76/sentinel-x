package br.com.sentinelx.core.application.event;

import br.com.sentinelx.core.domain.agent.AgentStatus;
import br.com.sentinelx.core.domain.camera.CameraStatus;
import br.com.sentinelx.core.domain.event.EventDirection;
import br.com.sentinelx.core.domain.event.EventType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

public record RegisterVehicleEventCommand(
        String agentId,
        AgentStatus agentStatus,
        String agentCredentialReference,
        String agentMetadataJson,
        String cameraId,
        String cameraDisplayName,
        String cameraLocation,
        CameraStatus cameraStatus,
        String cameraMetadataJson,
        String eventId,
        String idempotencyKey,
        OffsetDateTime occurredAt,
        String plateNumber,
        BigDecimal confidence,
        EventType eventType,
        EventDirection direction,
        EvidenceReferenceCommand evidence,
        String metadataJson
) {
    public RegisterVehicleEventCommand {
        requireText(agentId, "agentId");
        requireText(cameraId, "cameraId");
        requireText(eventId, "eventId");
        requireText(plateNumber, "plateNumber");
        Objects.requireNonNull(occurredAt, "occurredAt is required");
        Objects.requireNonNull(eventType, "eventType is required");

        agentStatus = agentStatus == null ? AgentStatus.ACTIVE : agentStatus;
        cameraStatus = cameraStatus == null ? CameraStatus.ACTIVE : cameraStatus;
        direction = direction == null ? EventDirection.UNKNOWN : direction;
    }

    private static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }

    public record EvidenceReferenceCommand(
            String key,
            String hash,
            String contentType,
            Long sizeBytes
    ) {
    }
}