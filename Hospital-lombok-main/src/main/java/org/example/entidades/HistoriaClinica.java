package org.example.entidades;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "paciente")
public class HistoriaClinica implements Serializable {
    @EqualsAndHashCode.Include
    private final String numeroHistoria;

    private final Paciente paciente;

    private final LocalDateTime fechaCreacion;

    @Getter(AccessLevel.NONE)
    private final List<String> diagnosticos;
    @Getter(AccessLevel.NONE)
    private final List<String> tratamientos;
    @Getter(AccessLevel.NONE)
    private final List<String> alergias;

    // Constructor privado para control
    private HistoriaClinica(String numeroHistoria, Paciente paciente, LocalDateTime fechaCreacion,
                            List<String> diagnosticos, List<String> tratamientos, List<String> alergias) {
        this.numeroHistoria = numeroHistoria;
        this.paciente = Objects.requireNonNull(paciente, "El paciente no puede ser nulo");
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion, "Fecha de creación no puede ser nula");
        this.diagnosticos = new ArrayList<>(Objects.requireNonNullElse(diagnosticos, Collections.emptyList()));
        this.tratamientos = new ArrayList<>(Objects.requireNonNullElse(tratamientos, Collections.emptyList()));
        this.alergias = new ArrayList<>(Objects.requireNonNullElse(alergias, Collections.emptyList()));
    }

    // fábrica para crear una historia a partir del paciente
    public static HistoriaClinica createForPaciente(Paciente paciente) {
        Objects.requireNonNull(paciente, "Paciente no puede ser nulo para crear HistoriaClinica");
        LocalDateTime now = LocalDateTime.now();
        String numero = "HC-" + paciente.getDni() + "-" + now.getYear();
        return new HistoriaClinica(numero, paciente, now, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    // métodos para agregar información clínica (mutan las listas internas)
    public void agregarDiagnostico(String diagnostico) {
        if (diagnostico != null && !diagnostico.trim().isEmpty()) diagnosticos.add(diagnostico);
    }

    public void agregarTratamiento(String tratamiento) {
        if (tratamiento != null && !tratamiento.trim().isEmpty()) tratamientos.add(tratamiento);
    }

    public void agregarAlergia(String alergia) {
        if (alergia != null && !alergia.trim().isEmpty()) alergias.add(alergia);
    }

    public List<String> getDiagnosticos() {
        return Collections.unmodifiableList(diagnosticos);
    }

    public List<String> getTratamientos() {
        return Collections.unmodifiableList(tratamientos);
    }

    public List<String> getAlergias() {
        return Collections.unmodifiableList(alergias);
    }


}