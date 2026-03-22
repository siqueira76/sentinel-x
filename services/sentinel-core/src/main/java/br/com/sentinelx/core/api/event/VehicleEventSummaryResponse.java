package br.com.sentinelx.core.api.event;

import br.com.sentinelx.core.application.event.VehicleEventSummary;
import br.com.sentinelx.core.domain.event.EventDirection;
import br.com.sentinelx.core.domain.event.EventType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record VehicleEventSummaryResponse(
        UUID id,
        String eventId,
        String agentId,
        String cameraId,
        String cameraDisplayName,
        OffsetDateTime occurredAt,
        String plateNumber,
        BigDecimal confidence,
        EventType eventType,
        EventDirection direction,
        boolean hasEvidence
) {
    static VehicleEventSummaryResponse from(VehicleEventSummary summary) {
        return new VehicleEventSummaryResponse(
                summary.id(),
                summary.eventId(),
                summary.agentId(),
                summary.cameraId(),
                summary.cameraDisplayName(),
                summary.occurredAt(),
                summary.plateNumber(),
                summary.confidence(),
                summary.eventType(),
                summary.direction(),
                summary.hasEvidence()
        );
    }
}