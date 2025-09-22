package org.digital_academy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ClinicaveterinariaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClinicaveterinariaApplication.class, args);
    }
}