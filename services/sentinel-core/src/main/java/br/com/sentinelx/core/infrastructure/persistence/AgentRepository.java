package br.com.sentinelx.core.infrastructure.persistence;

import br.com.sentinelx.core.domain.agent.Agent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByExternalId(String externalId);
}