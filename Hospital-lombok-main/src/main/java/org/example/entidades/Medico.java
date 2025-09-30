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
@ToString(exclude = {"departamento", "citas"})
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
public class Medico extends Persona implements Serializable {
    @NonNull
    @EqualsAndHashCode.Include
    private final Matricula matricula;

    @NonNull
    private final EspecialidadMedica especialidad;

    @Setter(AccessLevel.NONE)
    private Departamento departamento;

    @Builder.Default
    @Getter(AccessLevel.NONE)
    private final List<Cita> citas = new ArrayList<>();

    // Validación auxiliar para matrícula (si en la llamada pasa String, lo valida antes de construir Matricula)
    public static Matricula crearMatriculaValida(String numero) {
        return Matricula.builder().numero(Matricula.validarMatricula(numero)).build();
    }
    public void setDepartamento(Departamento departamento) {
        if (this.departamento != departamento) {
            this.departamento = departamento;
        }
    }

    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public List<Cita> getCitas() {
        return List.copyOf(citas);
    }
}