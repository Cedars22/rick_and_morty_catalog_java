package com.example.testspring.dto;

import java.util.List;

/** Una pagina de personajes tal como la expone este servicio. */
public record PaginaDto(
        int pagina,
        int totalPaginas,
        int totalPersonajes,
        List<PersonajeDto> personajes) {
}
