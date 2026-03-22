package br.com.sentinelx.core.application.event;

import java.util.UUID;

public class VehicleEventNotFoundException extends RuntimeException {

    public VehicleEventNotFoundException(UUID id) {
        super("Vehicle event '" + id + "' was not found.");
    }
}