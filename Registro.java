package com.tecnovalle;

import java.time.LocalDateTime;

public record Registro(String idUsuario, String ruta, String estacion, String accion, LocalDateTime timestamp) {
    public Registro {
        if (idUsuario == null || ruta == null || estacion == null || accion == null || timestamp == null) {
            throw new IllegalArgumentException("Los campos del registro no pueden ser nulos");
        }
        if (!accion.equals("entrada") && !accion.equals("salida")) {
            throw new IllegalArgumentException("La accion debe ser entrada o salida");
        }
    }
}

