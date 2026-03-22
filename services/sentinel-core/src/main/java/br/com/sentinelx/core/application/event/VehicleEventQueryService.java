package br.com.sentinelx.core.application.event;

import br.com.sentinelx.core.domain.event.EvidenceReference;
import br.com.sentinelx.core.domain.event.VehicleEvent;
import br.com.sentinelx.core.infrastructure.persistence.VehicleEventRepository;
import jakarta.persistence.criteria.Path;
import java.util.UUID;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleEventQueryService {

    private final VehicleEventRepository vehicleEventRepository;

    public VehicleEventQueryService(VehicleEventRepository vehicleEventRepository) {
        this.vehicleEventRepository = vehicleEventRepository;
    }

    @Transactional(readOnly = true)
    public Page<VehicleEventSummary> search(VehicleEventSearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page(),
                query.size(),
                Sort.by(Sort.Order.desc("occurredAt"), Sort.Order.desc("createdAt"))
        );

        return vehicleEventRepository.findAll(toSpecification(query), pageable)
                .map(this::toSummary);
    }

    @Transactional(readOnly = true)
    public VehicleEventDetails getById(UUID id) {
        VehicleEvent event = vehicleEventRepository.findById(id)
                .orElseThrow(() -> new VehicleEventNotFoundException(id));
        return toDetails(event);
    }

    private Specification<VehicleEvent> toSpecification(VehicleEventSearchQuery query) {
        Specification<VehicleEvent> specification = Specification.where(null);

        if (query.from() != null) {
            specification = specification.and((root, ignoredQuery, cb) -> cb.greaterThanOrEqualTo(root.get("occurredAt"), query.from()));
        }
        if (query.to() != null) {
            specification = specification.and((root, ignoredQuery, cb) -> cb.lessThanOrEqualTo(root.get("occurredAt"), query.to()));
        }
        if (query.agentId() != null) {
            specification = specification.and((root, ignoredQuery, cb) -> cb.equal(root.join("agent").get("externalId"), query.agentId()));
        }
        if (query.cameraId() != null) {
            specification = specification.and((root, ignoredQuery, cb) -> cb.equal(root.join("camera").get("externalId"), query.cameraId()));
        }
        if (query.plateNumber() != null) {
            specification = specification.and((root, ignoredQuery, cb) -> cb.equal(cb.upper(root.get("plateNumber")), query.plateNumber()));
        }
        if (query.eventType() != null) {
            specification = specification.and((root, ignoredQuery, cb) -> cb.equal(root.get("eventType"), query.eventType()));
        }
        if (query.hasEvidence() != null) {
            specification = specification.and((root, ignoredQuery, cb) -> {
                Path<String> evidenceKey = root.get("evidenceReference").get("key");
                return Boolean.TRUE.equals(query.hasEvidence()) ? cb.isNotNull(evidenceKey) : cb.isNull(evidenceKey);
            });
        }

        return specification;
    }

    private VehicleEventSummary toSummary(VehicleEvent event) {
        return new VehicleEventSummary(
                event.getId(),
                event.getExternalEventId(),
                event.getAgent().getExternalId(),
                event.getCamera().getExternalId(),
                event.getCamera().getDisplayName(),
                event.getOccurredAt(),
                event.getPlateNumber(),
                event.getConfidence(),
                event.getEventType(),
                event.getDirection(),
                event.getEvidenceReference() != null && Objects.nonNull(event.getEvidenceReference().getKey())
        );
    }

    private VehicleEventDetails toDetails(VehicleEvent event) {
        return new VehicleEventDetails(
                event.getId(),
                event.getExternalEventId(),
                event.getAgent().getExternalId(),
                event.getAgent().getStatus(),
                event.getAgent().getCredentialReference(),
                event.getAgent().getMetadataJson(),
                event.getCamera().getExternalId(),
                event.getCamera().getDisplayName(),
                event.getCamera().getLocation(),
                event.getCamera().getStatus(),
                event.getCamera().getMetadataJson(),
                event.getIdempotencyKey(),
                event.getOccurredAt(),
                event.getCreatedAt(),
                event.getPlateNumber(),
                event.getConfidence(),
                event.getEventType(),
                event.getDirection(),
                toEvidenceDetails(event.getEvidenceReference()),
                event.getMetadataJson()
        );
    }

    private VehicleEventDetails.EvidenceReferenceDetails toEvidenceDetails(EvidenceReference evidenceReference) {
        if (evidenceReference == null) {
            return null;
        }

        return new VehicleEventDetails.EvidenceReferenceDetails(
                evidenceReference.getKey(),
                evidenceReference.getHash(),
                evidenceReference.getContentType(),
                evidenceReference.getSizeBytes()
        );
    }
}