package com.RolaPet.Geolocalizacion.domain.enums;

public enum TipoUbicacion {
    ACTUAL("Ubicación Actual"),
    HISTORICO("Ubicación Histórica"),
    COMPARTIDA("Ubicación Compartida");

    private final String descripcion;

    TipoUbicacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
