package br.com.sentinelx.core.api.event;

import br.com.sentinelx.core.application.event.VehicleEventQueryService;
import br.com.sentinelx.core.application.event.VehicleEventSearchQuery;
import br.com.sentinelx.core.application.event.VehicleEventSummary;
import br.com.sentinelx.core.domain.event.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/events", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vehicle Events", description = "Administrative endpoints for historical vehicle event queries.")
public class VehicleEventQueryController {

    private final VehicleEventQueryService vehicleEventQueryService;
    private final ObjectMapper objectMapper;

    public VehicleEventQueryController(VehicleEventQueryService vehicleEventQueryService, ObjectMapper objectMapper) {
        this.vehicleEventQueryService = vehicleEventQueryService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    @Operation(
            summary = "List persisted vehicle events with pagination and filters",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged result returned successfully."),
            @ApiResponse(responseCode = "400", description = "Query parameters are invalid.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Admin authentication is required.", content = @Content)
    })
    public VehicleEventListResponse list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to,
            @RequestParam(required = false) String agentId,
            @RequestParam(required = false) String cameraId,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) EventType eventType,
            @RequestParam(required = false) Boolean hasEvidence,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        VehicleEventSearchQuery query = new VehicleEventSearchQuery(
                from,
                to,
                agentId,
                cameraId,
                plateNumber,
                eventType,
                hasEvidence,
                page,
                size
        );

        org.springframework.data.domain.Page<VehicleEventSummary> result = vehicleEventQueryService.search(query);
        return VehicleEventListResponse.from(result);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Fetch the complete detail of a persisted vehicle event",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event detail returned successfully."),
            @ApiResponse(responseCode = "401", description = "Admin authentication is required.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Vehicle event was not found.", content = @Content)
    })
    public VehicleEventDetailResponse detail(@Parameter(description = "Internal persisted event identifier.") @PathVariable UUID id) {
        return VehicleEventDetailResponse.from(vehicleEventQueryService.getById(id), objectMapper);
    }
}