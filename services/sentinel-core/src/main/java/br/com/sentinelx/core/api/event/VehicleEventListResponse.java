package br.com.sentinelx.core.api.event;

import br.com.sentinelx.core.application.event.VehicleEventSummary;
import java.util.List;
import org.springframework.data.domain.Page;

public record VehicleEventListResponse(
        List<VehicleEventSummaryResponse> items,
        PageMetadataResponse page
) {
    static VehicleEventListResponse from(Page<VehicleEventSummary> result) {
        return new VehicleEventListResponse(
                result.getContent().stream().map(VehicleEventSummaryResponse::from).toList(),
                new PageMetadataResponse(
                        result.getNumber(),
                        result.getSize(),
                        result.getTotalElements(),
                        result.getTotalPages()
                )
        );
    }

    public record PageMetadataResponse(
            int number,
            int size,
            long totalElements,
            int totalPages
    ) {
    }
}