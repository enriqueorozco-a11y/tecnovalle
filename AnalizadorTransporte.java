package com.tecnovalle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class AnalizadorTransporte {
    private AnalizadorTransporte() {}

    public static Map<String, Long> afluenciaPorEstacion(List<Registro> registros) {
        return registros.stream()
                .filter(r -> r.accion().equals("entrada"))
                .collect(Collectors.groupingBy(Registro::estacion, TreeMap::new, Collectors.counting()));
    }

    public static Map<Integer, Long> flujoPorHora(List<Registro> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(r -> r.timestamp().getHour(), TreeMap::new, Collectors.counting()));
    }

    public static List<Map.Entry<String, Long>> rutasMasUtilizadas(List<Registro> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(Registro::ruta, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .toList();
    }

    public static Map<String, List<String>> patronesPorUsuario(List<Registro> registros) {
        return registros.stream()
                .sorted(Comparator.comparing(Registro::timestamp))
                .collect(Collectors.groupingBy(Registro::idUsuario, TreeMap::new,
                        Collectors.mapping(Registro::estacion, Collectors.toUnmodifiableList())));
    }

    public static OptionalDouble tiempoPromedioEntreEstacionesMinutos(List<Registro> registros) {
        Map<String, List<Registro>> porUsuario = registros.stream()
                .collect(Collectors.groupingBy(Registro::idUsuario));

        List<Long> minutos = porUsuario.values().stream()
                .flatMap(lista -> lista.stream().sorted(Comparator.comparing(Registro::timestamp)))
                .collect(Collectors.groupingBy(Registro::idUsuario))
                .values().stream()
                .flatMap(lista -> paresConsecutivos(lista).stream())
                .map(par -> Duration.between(par.get(0).timestamp(), par.get(1).timestamp()).toMinutes())
                .filter(m -> m >= 0)
                .toList();

        return minutos.stream().mapToLong(Long::longValue).average();
    }

    private static List<List<Registro>> paresConsecutivos(List<Registro> lista) {
        List<List<Registro>> pares = new ArrayList<>();
        for (int i = 0; i < lista.size() - 1; i++) {
            pares.add(List.of(lista.get(i), lista.get(i + 1)));
        }
        return List.copyOf(pares);
    }

    public static Map<String, Long> afluenciaConFuncionDeOrdenSuperior(
            List<Registro> registros,
            Predicate<Registro> filtro,
            Function<Registro, String> clave) {
        return registros.stream().filter(filtro)
                .collect(Collectors.groupingBy(clave, TreeMap::new, Collectors.counting()));
    }

    public static Map<String, String> clasificarRutasCriticas(List<Registro> registros, long umbral) {
        Map<String, String> resultado = registros.stream()
                .collect(Collectors.groupingBy(Registro::ruta, TreeMap::new, Collectors.counting()))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue() > umbral ? "CRITICA" : "NORMAL",
                        (a, b) -> a,
                        TreeMap::new));
        return Collections.unmodifiableMap(resultado);
    }
}
