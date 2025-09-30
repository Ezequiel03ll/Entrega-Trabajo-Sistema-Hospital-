package org.example.entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(exclude = {"hospital", "citas"})
@SuperBuilder
public class Paciente extends Persona implements Serializable {
    @NonNull
    @EqualsAndHashCode.Include
    private final String telefono;

    @NonNull
    private final String direccion;

    private  HistoriaClinica historiaClinica ;

    @Setter(AccessLevel.NONE)
    private Hospital hospital;

    @Builder.Default
    @Getter(AccessLevel.NONE)
    private final List<Cita> citas = new ArrayList<>();

    // Validadores estáticos
    public static String validarTelefono(String telefono) {
        Objects.requireNonNull(telefono, "El teléfono no puede ser nulo");
        if (telefono.trim().isEmpty()) throw new IllegalArgumentException("El teléfono no puede estar vacío");
        return telefono;
    }

    public static String validarDireccion(String direccion) {
        Objects.requireNonNull(direccion, "La dirección no puede ser nula");
        if (direccion.trim().isEmpty()) throw new IllegalArgumentException("La dirección no puede estar vacía");
        return direccion;
    }

    // Asociación con hospital (mutable, con mantenimiento de bidireccionalidad)
    public void setHospital(Hospital hospital) {
        if (this.hospital != hospital) {
            if (this.hospital != null) {
                this.hospital.getPacientes().remove(this);
            }
            this.hospital = hospital;
            if (hospital != null) hospital.getPacientes().add(this);
        }
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = Objects.requireNonNull(historiaClinica, "HistoriaClinica no puede ser nula");
    }

    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public List<Cita> getCitas() {
        return List.copyOf(citas);
    }

}