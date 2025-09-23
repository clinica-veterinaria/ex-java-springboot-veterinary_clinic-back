package org.digital_academy.implementation;

import java.util.List;
import java.util.Optional;

public interface IPatientService<T, R> {

    List<T> listarPatients();

    T guardarPatient(R requestDTO);

    Optional<T> buscarPorId(Long id);

    Optional<T> actualizarPatient(Long id, R requestDTO);

    void eliminarPatient(Long id);

    Optional<T> buscarPorOwnerDNI(String ownerDNI);

    Optional<T> buscarPorCampo(String campo);

    Optional<T> buscarPorTelefono(String telefono);

}