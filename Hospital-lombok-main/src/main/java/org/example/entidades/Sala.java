package org.example.entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(force = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"departamento", "citas"})
@SuperBuilder
public class Sala implements Serializable {
    @NonNull
    @EqualsAndHashCode.Include
    private final String numero;

    @NonNull
    private final String tipo;

    @NonNull
    private final Departamento departamento;

    @Builder.Default
    @Getter(AccessLevel.NONE)
    private final List<Cita> citas = new ArrayList<>();

    // ===== Validadores =====
    public static String validarString(String valor, String mensaje) {
        Objects.requireNonNull(valor, mensaje);
        if (valor.trim().isEmpty()) throw new IllegalArgumentException(mensaje);
        return valor;
    }

    // ===== Manejo de citas =====
    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public List<Cita> getCitas() {
        return List.copyOf(citas);
    }
}