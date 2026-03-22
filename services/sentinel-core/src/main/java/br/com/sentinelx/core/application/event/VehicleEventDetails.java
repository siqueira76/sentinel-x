package br.com.sentinelx.core.application.event;

import br.com.sentinelx.core.domain.agent.AgentStatus;
import br.com.sentinelx.core.domain.camera.CameraStatus;
import br.com.sentinelx.core.domain.event.EventDirection;
import br.com.sentinelx.core.domain.event.EventType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record VehicleEventDetails(
        UUID id,
        String eventId,
        String agentId,
        AgentStatus agentStatus,
        String agentCredentialReference,
        String agentMetadataJson,
        String cameraId,
        String cameraDisplayName,
        String cameraLocation,
        CameraStatus cameraStatus,
        String cameraMetadataJson,
        String idempotencyKey,
        OffsetDateTime occurredAt,
        OffsetDateTime createdAt,
        String plateNumber,
        BigDecimal confidence,
        EventType eventType,
        EventDirection direction,
        EvidenceReferenceDetails evidence,
        String metadataJson
) {
    public record EvidenceReferenceDetails(
            String key,
            String hash,
            String contentType,
            Long sizeBytes
    ) {
    }
}