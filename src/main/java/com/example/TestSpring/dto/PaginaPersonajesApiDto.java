package com.example.TestSpring.dto;

import java.util.List;

/** Respuesta paginada de la API externa: /character?page=N */
public record PaginaPersonajesApiDto(InfoApiDto info, List<PersonajeApiDto> results) {
}
