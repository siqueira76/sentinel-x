package br.com.sentinelx.core;

import br.com.sentinelx.core.application.event.VehicleEventPersistenceService;
import br.com.sentinelx.core.application.event.VehicleEventQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
class SentinelCoreApplicationTests {

    @MockBean
    private VehicleEventPersistenceService vehicleEventPersistenceService;

    @MockBean
    private VehicleEventQueryService vehicleEventQueryService;

    @Test
    void contextLoads() {
    }
}