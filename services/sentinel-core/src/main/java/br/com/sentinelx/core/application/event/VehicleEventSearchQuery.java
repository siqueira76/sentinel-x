package br.com.sentinelx.core.application.event;

import br.com.sentinelx.core.domain.event.EventType;
import java.time.OffsetDateTime;
import java.util.Locale;

public record VehicleEventSearchQuery(
        OffsetDateTime from,
        OffsetDateTime to,
        String agentId,
        String cameraId,
        String plateNumber,
        EventType eventType,
        Boolean hasEvidence,
        int page,
        int size
) {
    public VehicleEventSearchQuery {
        agentId = normalize(agentId);
        cameraId = normalize(cameraId);
        plateNumber = normalizePlate(plateNumber);

        if (page < 0) {
            throw new IllegalArgumentException("page must be greater than or equal to 0.");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100.");
        }
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("from must be less than or equal to to.");
        }
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static String normalizePlate(String value) {
        String normalized = normalize(value);
        return normalized == null ? null : normalized.toUpperCase(Locale.ROOT);
    }
}