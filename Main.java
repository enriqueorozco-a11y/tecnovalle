package com.tecnovalle;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Registro> registros = List.of(
                new Registro("U01", "R1", "Central", "entrada", LocalDateTime.of(2026, 9, 29, 7, 10)),
                new Registro("U01", "R1", "Norte", "salida", LocalDateTime.of(2026, 9, 29, 7, 35)),
                new Registro("U01", "R2", "Universidad", "entrada", LocalDateTime.of(2026, 9, 29, 8, 5)),
                new Registro("U02", "R1", "Central", "entrada", LocalDateTime.of(2026, 9, 29, 7, 20)),
                new Registro("U02", "R1", "Norte", "salida", LocalDateTime.of(2026, 9, 29, 7, 45)),
                new Registro("U02", "R2", "Universidad", "entrada", LocalDateTime.of(2026, 9, 29, 8, 20)),
                new Registro("U03", "R3", "Sur", "entrada", LocalDateTime.of(2026, 9, 29, 8, 10)),
                new Registro("U03", "R3", "Centro", "salida", LocalDateTime.of(2026, 9, 29, 8, 40)),
                new Registro("U03", "R3", "Sur", "entrada", LocalDateTime.of(2026, 9, 29, 9, 0)),
                new Registro("U04", "R2", "Universidad", "entrada", LocalDateTime.of(2026, 9, 29, 9, 10)),
                new Registro("U04", "R2", "Centro", "salida", LocalDateTime.of(2026, 9, 29, 9, 40)),
                new Registro("U05", "R2", "Universidad", "entrada", LocalDateTime.of(2026, 9, 29, 9, 20))
        );

        System.out.println("=== TECNOMOVIL DATA | PROCESAMIENTO FUNCIONAL ===");
        System.out.println("Registros procesados: " + registros.size());
        System.out.println("\n1. Afluencia por estación: " + AnalizadorTransporte.afluenciaPorEstacion(registros));
        System.out.println("2. Flujo por hora: " + AnalizadorTransporte.flujoPorHora(registros));
        System.out.println("3. Rutas más utilizadas: " + AnalizadorTransporte.rutasMasUtilizadas(registros));
        System.out.println("4. Patrones por usuario: " + AnalizadorTransporte.patronesPorUsuario(registros));
        System.out.println("5. Tiempo promedio entre estaciones: " +
                AnalizadorTransporte.tiempoPromedioEntreEstacionesMinutos(registros).orElse(0) + " minutos");
        System.out.println("6. Rutas críticas (umbral 4): " + AnalizadorTransporte.clasificarRutasCriticas(registros, 4));

        var entradas = AnalizadorTransporte.afluenciaConFuncionDeOrdenSuperior(
                registros,
                r -> r.accion().equals("entrada"),
                Registro::estacion);
        System.out.println("\nDemostración lambda + función de orden superior: " + entradas);
        System.out.println("Lista original sin modificar: " + registros.size() + " registros");
    }
}
