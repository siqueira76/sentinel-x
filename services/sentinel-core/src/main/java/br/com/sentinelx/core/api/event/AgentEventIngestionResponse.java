package br.com.sentinelx.core.api.event;

import br.com.sentinelx.core.application.event.RegisterVehicleEventResult;
import java.util.UUID;

public record AgentEventIngestionResponse(
        UUID id,
        String eventId,
        String status,
        boolean duplicate
) {
    static AgentEventIngestionResponse from(String eventId, RegisterVehicleEventResult result) {
        return new AgentEventIngestionResponse(
                result.id(),
                eventId,
                result.duplicate() ? "DUPLICATE" : "ACCEPTED",
                result.duplicate()
        );
    }
}