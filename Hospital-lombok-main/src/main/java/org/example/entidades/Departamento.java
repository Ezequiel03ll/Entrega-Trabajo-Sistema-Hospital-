package org.example.entidades;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"hospital", "medicos", "salas"})
@Builder
public class Departamento implements Serializable {

    @NonNull
    @EqualsAndHashCode.Include
    private final String nombre;

    @NonNull
    private final EspecialidadMedica especialidad;

    @Setter(AccessLevel.NONE)
    private Hospital hospital;

    @Builder.Default
    @Getter(AccessLevel.NONE)
    private final List<Medico> medicos = new ArrayList<>();

    @Builder.Default
    @Getter(AccessLevel.NONE)
    private final List<Sala> salas = new ArrayList<>();

    // ===== Relación con hospital =====
    public void setHospital(Hospital hospital) {
        if (this.hospital != hospital) {
            if (this.hospital != null) {
                this.hospital.getDepartamentos().remove(this);
            }
            this.hospital = hospital;
            if (hospital != null) {
                hospital.getDepartamentos().add(this);
            }
        }
    }

    // ===== Relaciones con médicos y salas =====
    public void agregarMedico(Medico medico) {
        if (medico != null && !medicos.contains(medico)) {
            medicos.add(medico);
            medico.setDepartamento(this);
        }
    }

    public Sala crearSala(String numero, String tipo) {
        Sala sala = Sala.builder()
                .numero(Sala.validarString(numero, "El número de sala no puede ser vacío"))
                .tipo(Sala.validarString(tipo, "El tipo de sala no puede ser vacío"))
                .departamento(this)
                .build();
        salas.add(sala);
        return sala;
    }

    public static String validarString(String valor, String mensaje) {
        Objects.requireNonNull(valor, mensaje);
        if (valor.trim().isEmpty()) throw new IllegalArgumentException(mensaje);
        return valor;
    }


    public List<Medico> getMedicos() {
        return Collections.unmodifiableList(medicos);
    }

    public List<Sala> getSalas() {
        return Collections.unmodifiableList(salas);
    }
}