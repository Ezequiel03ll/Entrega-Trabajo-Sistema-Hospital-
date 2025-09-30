
package org.example;

import org.example.entidades.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Main {
    public static void main(String[] args) {
        System.out.println( " SISTEMA DE GESTIÓN HOSPITALARIA \n");

        try {
            // 1. Inicializar hospital y estructura
            Hospital hospital = inicializarHospital();

            // 2. Crear y configurar médicos
            List<Medico> medicos = crearMedicos(hospital);

            // 3. Registrar pacientes
            List<Paciente> pacientes = registrarPacientes(hospital);

            // 4. Programar citas médicas
            CitaManager citaManager = CitaManager.builder().build();
            programarCitas(citaManager, medicos, pacientes, hospital);

            // 5. Mostrar información del sistema
            mostrarInformacionCompleta(hospital, citaManager);

            // 6. Probar persistencia de datos
            probarPersistencia(citaManager, pacientes, medicos, hospital);

            // 7. Ejecutar pruebas de validación
            ejecutarPruebasValidacion(citaManager, medicos, pacientes, hospital);

            // 8. Estadísticas finales
            mostrarEstadisticasFinales(hospital);

            System.out.println("\n SISTEMA EJECUTADO EXITOSAMENTE ");

        } catch (Exception e) {
            System.err.println("Error en el sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // INICIALIZACIÓN

    private static Hospital inicializarHospital() {
        System.out.println("Inicializando hospital y departamentos...");

        Hospital hospital = Hospital.builder()
                .nombre(Hospital.validarString("Hospital Central", "El nombre es obligatorio"))
                .direccion(Hospital.validarString("Av. Libertador 1234", "La dirección es obligatoria"))
                .telefono(Hospital.validarString("011-4567-8901", "El teléfono es obligatorio"))
                .build();

        Departamento cardiologia = Departamento.builder()
                .nombre(Departamento.validarString("Cardiología", "Nombre inválido"))
                .especialidad(EspecialidadMedica.CARDIOLOGIA)
                .build();

        Departamento pediatria = Departamento.builder()
                .nombre(Departamento.validarString("Pediatría", "Nombre inválido"))
                .especialidad(EspecialidadMedica.PEDIATRIA)
                .build();

        Departamento traumatologia = Departamento.builder()
                .nombre(Departamento.validarString("Traumatología", "Nombre inválido"))
                .especialidad(EspecialidadMedica.TRAUMATOLOGIA)
                .build();

        hospital.agregarDepartamento(cardiologia);
        hospital.agregarDepartamento(pediatria);
        hospital.agregarDepartamento(traumatologia);

        cardiologia.crearSala("CARD-101", "Consultorio");
        cardiologia.crearSala("CARD-102", "Quirófano");
        pediatria.crearSala("PED-201", "Consultorio");
        traumatologia.crearSala("TRAUMA-301", "Emergencias");

        System.out.println("Hospital inicializado con " + hospital.getDepartamentos().size() + " departamentos\n");
        return hospital;
    }

    private static List<Medico> crearMedicos(Hospital hospital) {
        System.out.println("Registrando médicos especialistas...");

        List<Medico> medicos = new ArrayList<>();

        Medico cardiologo = Medico.builder()
                .nombre(Persona.validarString("Carlos", "Nombre inválido"))
                .apellido(Persona.validarString("González", "Apellido inválido"))
                .dni(Persona.validarDni("12345678"))
                .fechaNacimiento(LocalDate.of(1975, 5, 15))
                .tipoSangre(TipoSangre.A_POSITIVO)
                .matricula(Medico.crearMatriculaValida("MP-12345"))
                .especialidad(EspecialidadMedica.CARDIOLOGIA)
                .build();

        Medico pediatra = Medico.builder()
                .nombre(Persona.validarString("Ana", "Nombre inválido"))
                .apellido(Persona.validarString("Martínez", "Apellido inválido"))
                .dni(Persona.validarDni("23456789"))
                .fechaNacimiento(LocalDate.of(1980, 8, 22))
                .tipoSangre(TipoSangre.O_NEGATIVO)
                .matricula(Medico.crearMatriculaValida("MP-23456"))
                .especialidad(EspecialidadMedica.PEDIATRIA)
                .build();

        Medico traumatologo = Medico.builder()
                .nombre(Persona.validarString("Luis", "Nombre inválido"))
                .apellido(Persona.validarString("Rodríguez", "Apellido inválido"))
                .dni(Persona.validarDni("34567890"))
                .fechaNacimiento(LocalDate.of(1978, 3, 10))
                .tipoSangre(TipoSangre.B_POSITIVO)
                .matricula(Medico.crearMatriculaValida("MP-34567"))
                .especialidad(EspecialidadMedica.TRAUMATOLOGIA)
                .build();

        for (Departamento dep : hospital.getDepartamentos()) {
            switch (dep.getEspecialidad()) {
                case CARDIOLOGIA -> {
                    dep.agregarMedico(cardiologo);
                    medicos.add(cardiologo);
                }
                case PEDIATRIA -> {
                    dep.agregarMedico(pediatra);
                    medicos.add(pediatra);
                }
                case TRAUMATOLOGIA -> {
                    dep.agregarMedico(traumatologo);
                    medicos.add(traumatologo);
                }
            }
        }

        System.out.println("Registrados " + medicos.size() + " médicos especialistas\n");
        return medicos;
    }

    private static List<Paciente> registrarPacientes(Hospital hospital) {
        System.out.println("Registrando pacientes...");

        List<Paciente> pacientes = new ArrayList<>();

        Paciente pacienteCardiaco = Paciente.builder()
                .nombre(Persona.validarString("María", "Nombre inválido"))
                .apellido(Persona.validarString("López", "Apellido inválido"))
                .dni(Persona.validarDni("11111111"))
                .fechaNacimiento(LocalDate.of(1985, 12, 5))
                .tipoSangre(TipoSangre.A_POSITIVO)
                .telefono(Paciente.validarTelefono("011-1111-1111"))
                .direccion(Paciente.validarDireccion("Calle Falsa 123"))
                .build();
        pacienteCardiaco.setHistoriaClinica(HistoriaClinica.createForPaciente(pacienteCardiaco));

        Paciente pacientePediatrico = Paciente.builder()
                .nombre(Persona.validarString("Pedro", "Nombre inválido"))
                .apellido(Persona.validarString("García", "Apellido inválido"))
                .dni(Persona.validarDni("22222222"))
                .fechaNacimiento(LocalDate.of(2010, 6, 15))
                .tipoSangre(TipoSangre.O_POSITIVO)
                .telefono(Paciente.validarTelefono("011-2222-2222"))
                .direccion(Paciente.validarDireccion("Av. Siempreviva 456"))
                .build();
        pacientePediatrico.setHistoriaClinica(HistoriaClinica.createForPaciente(pacientePediatrico));

        Paciente pacienteTraumatologico = Paciente.builder()
                .nombre(Persona.validarString("Elena", "Nombre inválido"))
                .apellido(Persona.validarString("Fernández", "Apellido inválido"))
                .dni(Persona.validarDni("33333333"))
                .fechaNacimiento(LocalDate.of(1992, 9, 28))
                .tipoSangre(TipoSangre.AB_NEGATIVO)
                .telefono(Paciente.validarTelefono("011-3333-3333"))
                .direccion(Paciente.validarDireccion("Belgrano 789"))
                .build();
        pacienteTraumatologico.setHistoriaClinica(HistoriaClinica.createForPaciente(pacienteTraumatologico));

        hospital.agregarPaciente(pacienteCardiaco);
        hospital.agregarPaciente(pacientePediatrico);
        hospital.agregarPaciente(pacienteTraumatologico);

        pacientes.add(pacienteCardiaco);
        pacientes.add(pacientePediatrico);
        pacientes.add(pacienteTraumatologico);

        // configurar historias clínicas
        pacienteCardiaco.getHistoriaClinica().agregarDiagnostico("Hipertensión arterial");
        pacienteCardiaco.getHistoriaClinica().agregarTratamiento("Enalapril 10mg");
        pacienteCardiaco.getHistoriaClinica().agregarAlergia("Penicilina");

        pacientePediatrico.getHistoriaClinica().agregarDiagnostico("Control pediátrico rutinario");
        pacientePediatrico.getHistoriaClinica().agregarTratamiento("Vacunas al día");

        pacienteTraumatologico.getHistoriaClinica().agregarDiagnostico("Fractura de muñeca");
        pacienteTraumatologico.getHistoriaClinica().agregarTratamiento("Inmovilización y fisioterapia");
        pacienteTraumatologico.getHistoriaClinica().agregarAlergia("Ibuprofeno");

        System.out.println("Registrados " + pacientes.size() + " pacientes con historias clínicas\n");
        return pacientes;
    }

    private static void programarCitas(CitaManager citaManager, List<Medico> medicos, List<Paciente> pacientes, Hospital hospital) throws CitaException {
        System.out.println("Programando citas médicas...");

        Map<EspecialidadMedica, Sala> salasPorEspecialidad = obtenerSalasPorEspecialidad(hospital);

        LocalDateTime fechaBase = LocalDateTime.now().plusDays(1);

        Cita citaCardiologica = citaManager.programarCita(
                pacientes.get(0),
                obtenerMedicoPorEspecialidad(medicos, EspecialidadMedica.CARDIOLOGIA),
                salasPorEspecialidad.get(EspecialidadMedica.CARDIOLOGIA),
                fechaBase.withHour(10).withMinute(0),
                new BigDecimal("150000.00")
        );
        citaCardiologica.setObservaciones("Paciente con antecedentes de hipertensión");
        citaCardiologica.setEstado(EstadoCita.COMPLETADA);

        Cita citaPediatrica = citaManager.programarCita(
                pacientes.get(1),
                obtenerMedicoPorEspecialidad(medicos, EspecialidadMedica.PEDIATRIA),
                salasPorEspecialidad.get(EspecialidadMedica.PEDIATRIA),
                fechaBase.plusDays(1).withHour(14).withMinute(30),
                new BigDecimal("80000.00")
        );
        citaPediatrica.setObservaciones("Control de rutina - vacunas");
        citaPediatrica.setEstado(EstadoCita.EN_CURSO);

        Cita citaTraumatologica = citaManager.programarCita(
                pacientes.get(2),
                obtenerMedicoPorEspecialidad(medicos, EspecialidadMedica.TRAUMATOLOGIA),
                salasPorEspecialidad.get(EspecialidadMedica.TRAUMATOLOGIA),
                fechaBase.plusDays(2).withHour(9).withMinute(15),
                new BigDecimal("120000.00")
        );
        citaTraumatologica.setObservaciones("Seguimiento post-fractura");

        System.out.println("Programadas 3 citas médicas exitosamente\n");
    }

    private static Map<EspecialidadMedica, Sala> obtenerSalasPorEspecialidad(Hospital hospital) {
        Map<EspecialidadMedica, Sala> salas = new HashMap<>();
        for (Departamento dep : hospital.getDepartamentos()) {
            if (!dep.getSalas().isEmpty()) salas.put(dep.getEspecialidad(), dep.getSalas().get(0));
        }
        return salas;
    }

    private static Medico obtenerMedicoPorEspecialidad(List<Medico> medicos, EspecialidadMedica especialidad) {
        return medicos.stream().filter(m -> m.getEspecialidad() == especialidad).findFirst().orElse(null);
    }

    // ================== MOSTRAR INFORMACIÓN ==================
    private static void mostrarInformacionCompleta(Hospital hospital, CitaManager citaManager) {
        System.out.println("===== INFORMACIÓN DEL HOSPITAL =====");
        System.out.println(hospital);
        System.out.println("Departamentos: " + hospital.getDepartamentos().size());
        System.out.println("Pacientes registrados: " + hospital.getPacientes().size());

        System.out.println("\n===== DEPARTAMENTOS Y PERSONAL =====");
        for (Departamento dep : hospital.getDepartamentos()) {
            System.out.println(dep);
            System.out.println("  Médicos (" + dep.getMedicos().size() + "):");
            dep.getMedicos().forEach(m -> System.out.println("    " + m));
            System.out.println("  Salas (" + dep.getSalas().size() + "):");
            dep.getSalas().forEach(s -> System.out.println("    " + s));
        }

        System.out.println("\n===== PACIENTES E HISTORIAS CLÍNICAS =====");
        for (Paciente p : hospital.getPacientes()) {
            System.out.println(p);
            HistoriaClinica hc = p.getHistoriaClinica();
            System.out.println("  Historia: " + hc.getNumeroHistoria() + " | Edad: " + p.getEdad() + " años");
            System.out.println("  Diagnósticos: " + hc.getDiagnosticos());
            System.out.println("  Tratamientos: " + hc.getTratamientos());
            System.out.println("  Alergias: " + hc.getAlergias());
        }

        System.out.println("\n===== CITAS PROGRAMADAS =====");
        for (Paciente p : hospital.getPacientes()) {
            System.out.println("Citas de " + p.getNombreCompleto() + ":");
            for (Cita c : citaManager.getCitasPorPaciente(p)) {
                System.out.println("  " + c);
                System.out.println("    Observaciones: " + c.getObservaciones());
            }
            System.out.println();
        }
    }

    // ================== PERSISTENCIA ==================
    private static void probarPersistencia(CitaManager citaManager, List<Paciente> pacientes, List<Medico> medicos, Hospital hospital) throws Exception {
        System.out.println("===== PRUEBA DE PERSISTENCIA =====");

        String filename = "citas_hospital.csv";
        citaManager.guardarCitas(filename);
        System.out.println(" Citas guardadas en " + filename);

        Map<String, Paciente> pacientesMap = new HashMap<>();
        for (Paciente p : pacientes) pacientesMap.put(p.getDni(), p);

        Map<String, Medico> medicosMap = new HashMap<>();
        for (Medico m : medicos) medicosMap.put(m.getDni(), m);

        Map<String, Sala> salasMap = new HashMap<>();
        for (Departamento d : hospital.getDepartamentos()) {
            for (Sala s : d.getSalas()) salasMap.put(s.getNumero(), s);
        }

        citaManager.cargarCitas(filename, pacientesMap, medicosMap, salasMap);
        System.out.println(" Citas cargadas exitosamente desde archivo");
        System.out.println(" Total de citas cargadas: " + citaManager.getCitas().size() + "\n");
    }

    // ================== VALIDACIONES ==================
    private static void ejecutarPruebasValidacion(CitaManager citaManager, List<Medico> medicos, List<Paciente> pacientes, Hospital hospital) {
        System.out.println("===== PRUEBAS DE VALIDACIÓN =====");
        Map<EspecialidadMedica, Sala> salas = obtenerSalasPorEspecialidad(hospital);

        try {
            citaManager.programarCita(
                    pacientes.get(0),
                    medicos.get(0),
                    salas.get(EspecialidadMedica.CARDIOLOGIA),
                    LocalDateTime.now().minusDays(1),
                    new BigDecimal("1000.00"));
        } catch (CitaException e) {
            System.out.println(" Validación fecha pasado: " + e.getMessage());
        }

        try {
            citaManager.programarCita(
                    pacientes.get(0),
                    medicos.get(0),
                    salas.get(EspecialidadMedica.CARDIOLOGIA),
                    LocalDateTime.now().plusDays(3),
                    new BigDecimal("-500.00"));
        } catch (CitaException e) {
            System.out.println(" Validación costo negativo: " + e.getMessage());
        }

        try {
            citaManager.programarCita(
                    pacientes.get(0),
                    medicos.get(0),
                    salas.get(EspecialidadMedica.PEDIATRIA),
                    LocalDateTime.now().plusDays(4),
                    new BigDecimal("5000.00"));
        } catch (CitaException e) {
            System.out.println(" Validación especialidad incompatible: " + e.getMessage());
        }

        System.out.println();
    }

    //  MOSTRAMOS ESTADÍSTICAS
    private static void mostrarEstadisticasFinales(Hospital hospital) {
        System.out.println("===== ESTADÍSTICAS FINALES =====");
        System.out.println("Departamentos: " + hospital.getDepartamentos().size());
        System.out.println("Médicos: " + hospital.getDepartamentos().stream().mapToInt(d -> d.getMedicos().size()).sum());
        System.out.println("Salas: " + hospital.getDepartamentos().stream().mapToInt(d -> d.getSalas().size()).sum());
        System.out.println("Pacientes: " + hospital.getPacientes().size());

        System.out.println("\nDistribución por tipo de sangre:");
        Map<TipoSangre, Long> distribucion = new HashMap<>();
        for (Paciente p : hospital.getPacientes()) {
            distribucion.put(p.getTipoSangre(), distribucion.getOrDefault(p.getTipoSangre(), 0L) + 1);
        }
        distribucion.forEach((k, v) -> System.out.println("  " + k.getDescripcion() + ": " + v));

        System.out.println("\nDistribución por especialidad:");
        for (Departamento d : hospital.getDepartamentos()) {
            System.out.println("  " + d.getEspecialidad().getDescripcion() + ": "
                    + d.getMedicos().size() + " médicos, "
                    + d.getSalas().size() + " salas");
        }
    }
}