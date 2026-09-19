package com.example.TestSpring.dto;

/**
 * Bloque "origin" de la API externa.
 * Puede llegar con name "unknown" y url vacia.
 */
public record OrigenApiDto(String name, String url) {
}
