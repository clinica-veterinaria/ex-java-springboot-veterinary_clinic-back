package org.digital_academy.clinicaveterinaria.paciente.controller;

import lombok.RequiredArgsConstructor;
import org.digital_academy.clinicaveterinaria.paciente.dto.PacienteDTO;
import org.digital_academy.clinicaveterinaria.paciente.mapper.PacienteMapper;
import org.digital_academy.clinicaveterinaria.paciente.model.Paciente;
import org.digital_academy.clinicaveterinaria.paciente.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;
    private final PacienteMapper pacienteMapper;

    @GetMapping
    public List<PacienteDTO> listarPacientes() {
        return pacienteService.listarPacientes()
                .stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> obtenerPaciente(@PathVariable Long id) {
        return pacienteService.buscarPorId(id)
                .map(p -> ResponseEntity.ok(pacienteMapper.toDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> crearPaciente(@RequestBody PacienteDTO pacienteDTO) {
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
        Paciente guardado = pacienteService.guardarPaciente(paciente);
        return ResponseEntity.ok(pacienteMapper.toDTO(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> actualizarPaciente(@PathVariable Long id,
                                                          @RequestBody PacienteDTO pacienteDTO) {
        return pacienteService.buscarPorId(id)
                .map(p -> {
                    Paciente actualizado = pacienteMapper.toEntity(pacienteDTO);
                    actualizado.setId(id);
                    Paciente guardado = pacienteService.guardarPaciente(actualizado);
                    return ResponseEntity.ok(pacienteMapper.toDTO(guardado));
                })
                .orElse(ResponseEntity.notFound().build());
    }



   @DeleteMapping("/{id}")
public ResponseEntity<Void> eliminarPaciente(@PathVariable("id") Long id) {
    Optional<Paciente> pacienteOpt = pacienteService.buscarPorId(id);

    if (pacienteOpt.isPresent()) {
        pacienteService.eliminarPaciente(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    } else {
        return ResponseEntity.notFound().build(); // 404 Not Found
    }
    }

}