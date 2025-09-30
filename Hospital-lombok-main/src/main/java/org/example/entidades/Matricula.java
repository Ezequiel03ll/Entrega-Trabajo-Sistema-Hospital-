package org.example.entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Objects;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
public class Matricula implements Serializable {
    @NonNull
    @EqualsAndHashCode.Include
    private final String numero;

    public static String validarMatricula(String numero) {
        Objects.requireNonNull(numero, "El número de matrícula no puede ser nulo");
        if (!numero.matches("MP-\\d{4,6}")) {
            throw new IllegalArgumentException("Formato inválido. Debe ser MP-12345");
        }
        return numero;
    }
}