package com.example.testspring.dto;

import com.example.testspring.model.Personaje;

/** Personaje tal como lo expone este servicio. */
public record PersonajeDto(
        Long id,
        String nombre,
        String especie,
        String imagen,
        String origen) {

    public static PersonajeDto desdeEntidad(Personaje personaje) {
        return new PersonajeDto(
                personaje.getId(),
                personaje.getNombre(),
                personaje.getEspecie(),
                personaje.getImagen(),
                personaje.getOrigen());
    }

    public static PersonajeDto desdeApi(PersonajeApiDto api) {
        return new PersonajeDto(
                api.id(),
                api.name(),
                api.species(),
                api.image(),
                api.origin() == null ? null : api.origin().name());
    }
}
