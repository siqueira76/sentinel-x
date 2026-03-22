package br.com.sentinelx.core.application.event;

import br.com.sentinelx.core.domain.agent.Agent;
import br.com.sentinelx.core.domain.camera.Camera;
import br.com.sentinelx.core.domain.event.EvidenceReference;
import br.com.sentinelx.core.domain.event.VehicleEvent;
import br.com.sentinelx.core.infrastructure.persistence.AgentRepository;
import br.com.sentinelx.core.infrastructure.persistence.CameraRepository;
import br.com.sentinelx.core.infrastructure.persistence.VehicleEventRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleEventPersistenceService {

    private final AgentRepository agentRepository;
    private final CameraRepository cameraRepository;
    private final VehicleEventRepository vehicleEventRepository;

    public VehicleEventPersistenceService(
            AgentRepository agentRepository,
            CameraRepository cameraRepository,
            VehicleEventRepository vehicleEventRepository
    ) {
        this.agentRepository = agentRepository;
        this.cameraRepository = cameraRepository;
        this.vehicleEventRepository = vehicleEventRepository;
    }

    @Transactional
    public RegisterVehicleEventResult register(RegisterVehicleEventCommand command) {
        String deduplicationKey = deduplicationKeyFor(command);

        Optional<VehicleEvent> existingEvent = vehicleEventRepository.findByDeduplicationKey(deduplicationKey);
        if (existingEvent.isPresent()) {
            return new RegisterVehicleEventResult(existingEvent.get().getId(), true);
        }

        Agent agent = agentRepository.findByExternalId(command.agentId())
                .map(existing -> refreshAgent(existing, command))
                .orElseGet(() -> createAgent(command));

        Camera camera = cameraRepository.findByAgentIdAndExternalId(agent.getId(), command.cameraId())
                .map(existing -> refreshCamera(existing, command))
                .orElseGet(() -> createCamera(agent, command));

        VehicleEvent event = vehicleEventRepository.save(new VehicleEvent(
                agent,
                camera,
                command.eventId(),
                deduplicationKey,
                blankToNull(command.idempotencyKey()),
                command.occurredAt(),
                command.plateNumber(),
                command.confidence(),
                command.eventType(),
                command.direction(),
                toEvidenceReference(command.evidence()),
                command.metadataJson()
        ));

        return new RegisterVehicleEventResult(event.getId(), false);
    }

    static String deduplicationKeyFor(RegisterVehicleEventCommand command) {
        String idempotencyKey = blankToNull(command.idempotencyKey());
        if (idempotencyKey != null) {
            return "IDEMPOTENCY:" + idempotencyKey;
        }
        return "SOURCE:" + command.agentId() + ":" + command.eventId();
    }

    private Agent createAgent(RegisterVehicleEventCommand command) {
        return agentRepository.save(new Agent(
                command.agentId(),
                command.agentStatus(),
                command.agentCredentialReference(),
                command.agentMetadataJson()
        ));
    }

    private Agent refreshAgent(Agent agent, RegisterVehicleEventCommand command) {
        agent.setStatus(command.agentStatus());
        agent.setCredentialReference(command.agentCredentialReference());
        agent.setMetadataJson(command.agentMetadataJson());
        return agent;
    }

    private Camera createCamera(Agent agent, RegisterVehicleEventCommand command) {
        return cameraRepository.save(new Camera(
                agent,
                command.cameraId(),
                command.cameraDisplayName(),
                command.cameraLocation(),
                command.cameraStatus(),
                command.cameraMetadataJson()
        ));
    }

    private Camera refreshCamera(Camera camera, RegisterVehicleEventCommand command) {
        camera.setDisplayName(command.cameraDisplayName());
        camera.setLocation(command.cameraLocation());
        camera.setStatus(command.cameraStatus());
        camera.setMetadataJson(command.cameraMetadataJson());
        return camera;
    }

    private static EvidenceReference toEvidenceReference(RegisterVehicleEventCommand.EvidenceReferenceCommand evidence) {
        if (evidence == null) {
            return null;
        }
        return new EvidenceReference(evidence.key(), evidence.hash(), evidence.contentType(), evidence.sizeBytes());
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}