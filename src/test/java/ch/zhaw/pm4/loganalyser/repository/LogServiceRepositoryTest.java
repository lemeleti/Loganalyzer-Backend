package ch.zhaw.pm4.loganalyser.repository;

import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.LogServiceLocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
class LogServiceRepositoryTest {
    @Autowired
    private LogServiceRepository logServiceRepository;

    @Test
    void saveLogService() {
        LogService sample = LogService.builder()
                .name("Test Service")
                .description("Description")
                .logConfig(null)
                .image(null)
                .logDirectory("/var/log")
                .logServiceLocation(LogServiceLocation.LOCAL)
                .build();
        logServiceRepository.save(sample);
        assertEquals(1, logServiceRepository.count());
    }

    @Test
    @Sql("classpath:sql/getallservices.sql")
    void getAllServices() {
        List<LogService> services = logServiceRepository.findAll();
        assertEquals(2, services.size());
    }
}
