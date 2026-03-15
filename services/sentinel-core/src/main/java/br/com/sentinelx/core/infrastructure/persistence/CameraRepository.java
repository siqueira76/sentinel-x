package br.com.sentinelx.core.infrastructure.persistence;

import br.com.sentinelx.core.domain.camera.Camera;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CameraRepository extends JpaRepository<Camera, Long> {
    Optional<Camera> findByAgentIdAndExternalId(Long agentId, String externalId);
}