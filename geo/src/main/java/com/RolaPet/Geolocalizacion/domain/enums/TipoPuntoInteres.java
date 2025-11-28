package com.RolaPet.Geolocalizacion.domain.enums;

public enum TipoPuntoInteres {
    PARQUEADERO("Parqueadero"),
    TALLER("Taller Mecánico"),
    LAVADERO("Lavadero"),
    VENTA_PARTES("Venta de Repuestos"),
    ESTACION_CARGA("Estación de Carga"),
    PUNTO_ENCUENTRO("Punto de Encuentro"),
    EVENTO("Evento");

    private final String displayName;

    TipoPuntoInteres(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
