package org.digital_academy.clinicaveterinaria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


@RestController
public class HomeController {

    @GetMapping("/")
    public RedirectView redirectToPacientes() {
        return new RedirectView("/pacientes"); // o "/api/pacientes"
    }
}
