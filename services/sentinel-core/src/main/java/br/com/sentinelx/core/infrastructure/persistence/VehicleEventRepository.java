package br.com.sentinelx.core.infrastructure.persistence;

import br.com.sentinelx.core.domain.event.VehicleEvent;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VehicleEventRepository extends JpaRepository<VehicleEvent, UUID>, JpaSpecificationExecutor<VehicleEvent> {
    Optional<VehicleEvent> findByDeduplicationKey(String deduplicationKey);
}