package com.example.TestSpring.dto;

/**
 * Personaje tal como lo devuelve la API de Rick and Morty.
 * Solo se declaran los campos que se usan; el resto se ignora al deserializar.
 */
public record PersonajeApiDto(
        Long id,
        String name,
        String species,
        String image,
        OrigenApiDto origin) {
}
