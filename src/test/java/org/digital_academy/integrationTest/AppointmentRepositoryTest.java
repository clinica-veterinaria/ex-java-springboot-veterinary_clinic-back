package org.digital_academy.integrationTest;

import org.digital_academy.appointment.Appointment;
import org.digital_academy.appointment.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
class AppointmentRepositoryIntegrationTest {

    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("vetclinic")
            .withUsername("myuser")
            .withPassword("factoria");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Appointment testAppointment;

    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll();

        testAppointment = new Appointment();
        testAppointment.setReason("Vacuna");
        testAppointment.setType("STANDARD");
        testAppointment.setStatus("PENDIENTE");
        testAppointment.setAppointmentDatetime(LocalDateTime.now().plusDays(1));

        appointmentRepository.save(testAppointment);
    }

    @Test
    void testFindByPatientId() {
        List<Appointment> result = appointmentRepository.findByPatientId(1L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getReason()).isEqualTo("Vacuna");
    }

    @Test
    void testFindByAppointmentDatetimeAfterOrderByAppointmentDatetimeAsc() {
        List<Appointment> result = appointmentRepository.findByAppointmentDatetimeAfterOrderByAppointmentDatetimeAsc(
                LocalDateTime.now(), Pageable.unpaged());
        assertThat(result).hasSize(1);
    }

    @Test
    void testSearchWithFilters() {
        List<Appointment> result = appointmentRepository.searchWithFilters(
                "Vacuna", "STANDARD", "PENDIENTE", null
        );
        assertThat(result).hasSize(1);
    }
}
