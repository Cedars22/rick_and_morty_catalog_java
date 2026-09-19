package com.example.TestSpring.dto;

/** Bloque "info" con los datos de paginacion de la API externa. */
public record InfoApiDto(int count, int pages, String next, String prev) {
}
