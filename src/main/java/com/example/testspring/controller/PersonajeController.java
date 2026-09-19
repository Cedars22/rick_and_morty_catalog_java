package com.example.testspring.controller;

import com.example.testspring.dto.PaginaDto;
import com.example.testspring.dto.PersonajeDto;
import com.example.testspring.service.PersonajeService;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/personajes")
@Validated
public class PersonajeController {

    private final PersonajeService servicio;

    public PersonajeController(PersonajeService servicio) {
        this.servicio = servicio;
    }

    /** Lista los personajes de una pagina (la API externa devuelve 20 por pagina). */
    @GetMapping
    public PaginaDto listar(@RequestParam(defaultValue = "1") @Min(1) int pagina) {
        return servicio.listarPagina(pagina);
    }

    /** Consulta un personaje por id y lo guarda en la base de datos propia si no estaba. */
    @GetMapping("/{id}")
    public PersonajeDto obtenerPorId(@PathVariable @Min(1) Long id) {
        return servicio.obtenerPorId(id);
    }
}
