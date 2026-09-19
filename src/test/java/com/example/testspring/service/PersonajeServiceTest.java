package com.example.testspring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.example.testspring.dto.PaginaDto;
import com.example.testspring.dto.PersonajeDto;
import com.example.testspring.exception.RecursoNoEncontradoException;
import com.example.testspring.model.Personaje;
import com.example.testspring.repository.PersonajeRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class PersonajeServiceTest {

    private static final String BASE_URL = "https://api.test/api";

    private static final String JSON_RICK = """
            {
              "id": 1,
              "name": "Rick Sanchez",
              "status": "Alive",
              "species": "Human",
              "type": "",
              "gender": "Male",
              "origin": { "name": "Earth (C-137)", "url": "https://api.test/api/location/1" },
              "location": { "name": "Citadel of Ricks", "url": "https://api.test/api/location/3" },
              "image": "https://api.test/api/character/avatar/1.jpeg",
              "episode": ["https://api.test/api/episode/1"],
              "url": "https://api.test/api/character/1",
              "created": "2017-11-04T18:48:46.250Z"
            }
            """;

    private static final String JSON_PAGINA = """
            {
              "info": { "count": 826, "pages": 42, "next": "https://api.test/api/character?page=2", "prev": null },
              "results": [
                {
                  "id": 1,
                  "name": "Rick Sanchez",
                  "species": "Human",
                  "origin": { "name": "Earth (C-137)", "url": "" },
                  "image": "https://api.test/api/character/avatar/1.jpeg"
                },
                {
                  "id": 21,
                  "name": "Aqua Morty",
                  "species": "Humanoid",
                  "origin": { "name": "unknown", "url": "" },
                  "image": "https://api.test/api/character/avatar/21.jpeg"
                }
              ]
            }
            """;

    private PersonajeRepository repositorio;
    private MockRestServiceServer apiSimulada;
    private PersonajeService servicio;

    @BeforeEach
    void prepararServicio() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        apiSimulada = MockRestServiceServer.bindTo(builder).build();
        repositorio = mock(PersonajeRepository.class);
        servicio = new PersonajeService(builder.build(), repositorio);
    }

    @Test
    void devuelveElPersonajeDeLaBaseDeDatosSinLlamarALaApi() {
        when(repositorio.findById(1L)).thenReturn(Optional.of(
                new Personaje(1L, "Rick Sanchez", "Human", "imagen.jpeg", "Earth (C-137)")));

        PersonajeDto personaje = servicio.obtenerPorId(1L);

        assertThat(personaje.nombre()).isEqualTo("Rick Sanchez");
        assertThat(personaje.origen()).isEqualTo("Earth (C-137)");
        verify(repositorio, never()).save(any());
        // Si el servicio hubiera llamado a la API, la peticion seria inesperada y fallaria aqui.
        apiSimulada.verify();
    }

    @Test
    void consultaLaApiYGuardaCuandoNoEstaEnLaBaseDeDatos() {
        when(repositorio.findById(1L)).thenReturn(Optional.empty());
        when(repositorio.save(any(Personaje.class))).thenAnswer(llamada -> llamada.getArgument(0));
        apiSimulada.expect(requestTo(BASE_URL + "/character/1"))
                .andRespond(withSuccess(JSON_RICK, MediaType.APPLICATION_JSON));

        PersonajeDto personaje = servicio.obtenerPorId(1L);

        assertThat(personaje.id()).isEqualTo(1L);
        assertThat(personaje.nombre()).isEqualTo("Rick Sanchez");
        assertThat(personaje.especie()).isEqualTo("Human");
        assertThat(personaje.origen()).isEqualTo("Earth (C-137)");
        verify(repositorio).save(any(Personaje.class));
        apiSimulada.verify();
    }

    @Test
    void lanzaNoEncontradoCuandoLaApiNoConoceElId() {
        when(repositorio.findById(9999L)).thenReturn(Optional.empty());
        apiSimulada.expect(requestTo(BASE_URL + "/character/9999"))
                .andRespond(withResourceNotFound());

        assertThatThrownBy(() -> servicio.obtenerPorId(9999L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("9999");

        verify(repositorio, never()).save(any());
    }

    @Test
    void listarPaginaDevuelveLosPersonajesYNoPersisteNada() {
        apiSimulada.expect(requestTo(BASE_URL + "/character?page=2"))
                .andRespond(withSuccess(JSON_PAGINA, MediaType.APPLICATION_JSON));

        PaginaDto pagina = servicio.listarPagina(2);

        assertThat(pagina.pagina()).isEqualTo(2);
        assertThat(pagina.totalPaginas()).isEqualTo(42);
        assertThat(pagina.totalPersonajes()).isEqualTo(826);
        assertThat(pagina.personajes()).hasSize(2);
        assertThat(pagina.personajes().get(1).origen()).isEqualTo("unknown");
        verify(repositorio, never()).save(any());
        apiSimulada.verify();
    }
}
