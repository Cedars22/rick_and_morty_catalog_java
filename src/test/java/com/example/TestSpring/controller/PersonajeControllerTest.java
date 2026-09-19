package com.example.TestSpring.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.TestSpring.dto.PaginaDto;
import com.example.TestSpring.dto.PersonajeDto;
import com.example.TestSpring.exception.RecursoNoEncontradoException;
import com.example.TestSpring.service.PersonajeService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

@WebMvcTest(PersonajeController.class)
class PersonajeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonajeService servicio;

    @Test
    void devuelveElPersonajePorId() throws Exception {
        when(servicio.obtenerPorId(1L)).thenReturn(
                new PersonajeDto(1L, "Rick Sanchez", "Human", "imagen.jpeg", "Earth (C-137)"));

        mockMvc.perform(get("/api/personajes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Rick Sanchez"))
                .andExpect(jsonPath("$.especie").value("Human"))
                .andExpect(jsonPath("$.origen").value("Earth (C-137)"));
    }

    @Test
    void devuelve404CuandoElPersonajeNoExiste() throws Exception {
        when(servicio.obtenerPorId(anyLong()))
                .thenThrow(new RecursoNoEncontradoException("No existe un personaje con id 9999"));

        mockMvc.perform(get("/api/personajes/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void devuelve400CuandoLaPaginaNoEsValida() throws Exception {
        mockMvc.perform(get("/api/personajes?pagina=0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void devuelve400CuandoElIdNoEsValido() throws Exception {
        mockMvc.perform(get("/api/personajes/0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void devuelve502CuandoLaApiExternaFalla() throws Exception {
        when(servicio.listarPagina(1))
                .thenThrow(new RestClientException("La API externa no respondio"));

        mockMvc.perform(get("/api/personajes"))
                .andExpect(status().isBadGateway());
    }

    @Test
    void listaLaPrimeraPaginaPorDefecto() throws Exception {
        when(servicio.listarPagina(1)).thenReturn(new PaginaDto(1, 42, 826, List.of(
                new PersonajeDto(1L, "Rick Sanchez", "Human", "imagen.jpeg", "Earth (C-137)"))));

        mockMvc.perform(get("/api/personajes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagina").value(1))
                .andExpect(jsonPath("$.totalPaginas").value(42))
                .andExpect(jsonPath("$.personajes[0].nombre").value("Rick Sanchez"));
    }
}
