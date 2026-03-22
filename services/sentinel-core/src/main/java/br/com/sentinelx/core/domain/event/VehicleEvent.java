package br.com.sentinelx.core.domain.event;

import br.com.sentinelx.core.domain.agent.Agent;
import br.com.sentinelx.core.domain.camera.Camera;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicle_events")
public class VehicleEvent {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "camera_id", nullable = false)
    private Camera camera;

    @Column(name = "external_event_id", nullable = false, length = 100)
    private String externalEventId;

    @Column(name = "deduplication_key", nullable = false, unique = true, length = 255)
    private String deduplicationKey;

    @Column(name = "idempotency_key", length = 255)
    private String idempotencyKey;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    @Column(name = "plate_number", nullable = false, length = 32)
    private String plateNumber;

    @Column(precision = 5, scale = 4)
    private BigDecimal confidence;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private EventDirection direction;

    @Embedded
    private EvidenceReference evidenceReference;

    @Column(name = "metadata_json", columnDefinition = "TEXT")
    private String metadataJson;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected VehicleEvent() {
    }

    public VehicleEvent(
            Agent agent,
            Camera camera,
            String externalEventId,
            String deduplicationKey,
            String idempotencyKey,
            OffsetDateTime occurredAt,
            String plateNumber,
            BigDecimal confidence,
            EventType eventType,
            EventDirection direction,
            EvidenceReference evidenceReference,
            String metadataJson
    ) {
        this.agent = agent;
        this.camera = camera;
        this.externalEventId = externalEventId;
        this.deduplicationKey = deduplicationKey;
        this.idempotencyKey = idempotencyKey;
        this.occurredAt = occurredAt;
        this.plateNumber = plateNumber;
        this.confidence = confidence;
        this.eventType = eventType;
        this.direction = direction;
        this.evidenceReference = evidenceReference;
        this.metadataJson = metadataJson;
    }

    @PrePersist
    void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Agent getAgent() {
        return agent;
    }

    public Camera getCamera() {
        return camera;
    }

    public String getExternalEventId() {
        return externalEventId;
    }

    public String getDeduplicationKey() {
        return deduplicationKey;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public BigDecimal getConfidence() {
        return confidence;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventDirection getDirection() {
        return direction;
    }

    public EvidenceReference getEvidenceReference() {
        return evidenceReference;
    }

    public String getMetadataJson() {
        return metadataJson;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}