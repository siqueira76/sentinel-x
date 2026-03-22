package br.com.sentinelx.core.api.event;

import br.com.sentinelx.core.application.event.RegisterVehicleEventResult;
import br.com.sentinelx.core.application.event.VehicleEventPersistenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/agent-events", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Agent Events", description = "Endpoints used by authenticated agents to submit vehicle events.")
public class AgentEventController {

    private final VehicleEventPersistenceService vehicleEventPersistenceService;

    public AgentEventController(VehicleEventPersistenceService vehicleEventPersistenceService) {
        this.vehicleEventPersistenceService = vehicleEventPersistenceService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Ingest a vehicle event sent by an authenticated agent",
            security = @SecurityRequirement(name = "agentApiKey")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Event accepted successfully."),
            @ApiResponse(responseCode = "200", description = "Duplicate event recognized and returned idempotently."),
            @ApiResponse(responseCode = "400", description = "Request payload is invalid.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Agent authentication failed.", content = @Content)
    })
    public ResponseEntity<AgentEventIngestionResponse> ingest(@Valid @RequestBody AgentEventIngestionRequest request) {
        RegisterVehicleEventResult result = vehicleEventPersistenceService.register(request.toCommand());
        HttpStatus responseStatus = result.duplicate() ? HttpStatus.OK : HttpStatus.ACCEPTED;
        return ResponseEntity.status(responseStatus)
                .body(AgentEventIngestionResponse.from(request.eventId(), result));
    }
}