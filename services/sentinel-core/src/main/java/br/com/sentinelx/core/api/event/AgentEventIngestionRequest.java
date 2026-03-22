package br.com.sentinelx.core.api.event;

import br.com.sentinelx.core.application.event.RegisterVehicleEventCommand;
import br.com.sentinelx.core.domain.agent.AgentStatus;
import br.com.sentinelx.core.domain.camera.CameraStatus;
import br.com.sentinelx.core.domain.event.EventDirection;
import br.com.sentinelx.core.domain.event.EventType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AgentEventIngestionRequest(
        @NotBlank @Size(max = 100) String agentId,
        AgentStatus agentStatus,
        @Size(max = 255) String agentCredentialReference,
        JsonNode agentMetadata,
        @NotBlank @Size(max = 100) String cameraId,
        @Size(max = 255) String cameraDisplayName,
        @Size(max = 255) String cameraLocation,
        CameraStatus cameraStatus,
        JsonNode cameraMetadata,
        @NotBlank @Size(max = 100) String eventId,
        @Size(max = 255) String idempotencyKey,
        @NotNull OffsetDateTime occurredAt,
        @NotBlank @Size(max = 32) String plateNumber,
        @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal confidence,
        @NotNull EventType eventType,
        EventDirection direction,
        @Valid EvidenceReferenceRequest evidence,
        JsonNode metadata
) {
    public RegisterVehicleEventCommand toCommand() {
        return new RegisterVehicleEventCommand(
                agentId,
                agentStatus,
                agentCredentialReference,
                toJson(agentMetadata),
                cameraId,
                cameraDisplayName,
                cameraLocation,
                cameraStatus,
                toJson(cameraMetadata),
                eventId,
                idempotencyKey,
                occurredAt,
                plateNumber,
                confidence,
                eventType,
                direction,
                evidence == null ? null : evidence.toCommand(),
                toJson(metadata)
        );
    }

    private static String toJson(JsonNode value) {
        return value == null || value.isNull() ? null : value.toString();
    }

    public record EvidenceReferenceRequest(
            @Size(max = 255) String key,
            @Size(max = 255) String hash,
            @Size(max = 100) String contentType,
            @PositiveOrZero Long sizeBytes
    ) {
        RegisterVehicleEventCommand.EvidenceReferenceCommand toCommand() {
            return new RegisterVehicleEventCommand.EvidenceReferenceCommand(key, hash, contentType, sizeBytes);
        }
    }
}