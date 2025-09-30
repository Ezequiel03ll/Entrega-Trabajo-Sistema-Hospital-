package org.example.entidades;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
public abstract class Persona implements Serializable {

    @NonNull
    @EqualsAndHashCode.Include
    protected final String nombre;

    @NonNull
    protected final String apellido;

    @NonNull
    @EqualsAndHashCode.Include
    protected final String dni;

    @NonNull
    protected final LocalDate fechaNacimiento;

    @NonNull
    protected final TipoSangre tipoSangre;

    // ===== Métodos auxiliares =====
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public int getEdad() {
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }

    // ===== Validadores estáticos =====
    public static String validarString(String valor, String mensajeError) {
        Objects.requireNonNull(valor, mensajeError);
        if (valor.trim().isEmpty()) throw new IllegalArgumentException(mensajeError);
        return valor;
    }

    public static String validarDni(String dni) {
        Objects.requireNonNull(dni, "El DNI no puede ser nulo");
        if (!dni.matches("\\d{7,8}"))
            throw new IllegalArgumentException("El DNI debe tener 7 u 8 dígitos");
        return dni;
    }
}