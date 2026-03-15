package br.com.sentinelx.core.application.event;

import java.util.UUID;

public record RegisterVehicleEventResult(UUID id, boolean duplicate) {
}