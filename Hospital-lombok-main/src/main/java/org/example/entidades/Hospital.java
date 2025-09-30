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
@AllArgsConstructor
@ToString(exclude = {"departamentos", "pacientes"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Hospital implements Serializable {
    @NonNull
    @EqualsAndHashCode.Include
    private final String nombre;

    private final String direccion;

    @NonNull
    private final String telefono;

    @Builder.Default
    private final List<Departamento> departamentos = new ArrayList<>();

    @Builder.Default
    private final List<Paciente> pacientes = new ArrayList<>();

    public static String validarString(String valor, String msg) {
        Objects.requireNonNull(valor, msg);
        if (valor.trim().isEmpty()) throw new IllegalArgumentException(msg);
        return valor;
    }

    public void agregarDepartamento(Departamento d) {
        if (d != null && !departamentos.contains(d)) {
            departamentos.add(d);
            d.setHospital(this);
        }
    }

    public void agregarPaciente(Paciente p) {
        if (p != null && !pacientes.contains(p)) {
            pacientes.add(p);
            p.setHospital(this);
        }
    }


}