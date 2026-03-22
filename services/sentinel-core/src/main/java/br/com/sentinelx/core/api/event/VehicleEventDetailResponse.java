package br.com.sentinelx.core.api.event;

import br.com.sentinelx.core.application.event.VehicleEventDetails;
import br.com.sentinelx.core.domain.agent.AgentStatus;
import br.com.sentinelx.core.domain.camera.CameraStatus;
import br.com.sentinelx.core.domain.event.EventDirection;
import br.com.sentinelx.core.domain.event.EventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record VehicleEventDetailResponse(
        UUID id,
        String eventId,
        AgentResponse agent,
        CameraResponse camera,
        String idempotencyKey,
        OffsetDateTime occurredAt,
        OffsetDateTime createdAt,
        String plateNumber,
        BigDecimal confidence,
        EventType eventType,
        EventDirection direction,
        EvidenceReferenceResponse evidence,
        JsonNode metadata
) {
    static VehicleEventDetailResponse from(VehicleEventDetails details, ObjectMapper objectMapper) {
        return new VehicleEventDetailResponse(
                details.id(),
                details.eventId(),
                new AgentResponse(details.agentId(), details.agentStatus(), details.agentCredentialReference(), parseJson(details.agentMetadataJson(), objectMapper)),
                new CameraResponse(details.cameraId(), details.cameraDisplayName(), details.cameraLocation(), details.cameraStatus(), parseJson(details.cameraMetadataJson(), objectMapper)),
                details.idempotencyKey(),
                details.occurredAt(),
                details.createdAt(),
                details.plateNumber(),
                details.confidence(),
                details.eventType(),
                details.direction(),
                details.evidence() == null ? null : new EvidenceReferenceResponse(
                        details.evidence().key(),
                        details.evidence().hash(),
                        details.evidence().contentType(),
                        details.evidence().sizeBytes()
                ),
                parseJson(details.metadataJson(), objectMapper)
        );
    }

    private static JsonNode parseJson(String value, ObjectMapper objectMapper) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return objectMapper.readTree(value);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Stored metadata is malformed.", exception);
        }
    }

    public record AgentResponse(
            String id,
            AgentStatus status,
            String credentialReference,
            JsonNode metadata
    ) {
    }

    public record CameraResponse(
            String id,
            String displayName,
            String location,
            CameraStatus status,
            JsonNode metadata
    ) {
    }

    public record EvidenceReferenceResponse(
            String key,
            String hash,
            String contentType,
            Long sizeBytes
    ) {
    }
}