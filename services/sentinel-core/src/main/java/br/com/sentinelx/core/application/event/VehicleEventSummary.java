package br.com.sentinelx.core.application.event;

import br.com.sentinelx.core.domain.event.EventDirection;
import br.com.sentinelx.core.domain.event.EventType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record VehicleEventSummary(
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
}