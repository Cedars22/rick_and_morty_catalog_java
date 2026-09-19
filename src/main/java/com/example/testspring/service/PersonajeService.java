package com.example.testspring.service;

import com.example.testspring.dto.PaginaDto;
import com.example.testspring.dto.PaginaPersonajesApiDto;
import com.example.testspring.dto.PersonajeApiDto;
import com.example.testspring.dto.PersonajeDto;
import com.example.testspring.exception.RecursoNoEncontradoException;
import com.example.testspring.model.Personaje;
import com.example.testspring.repository.PersonajeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClient;

@Service
public class PersonajeService {

    private final RestClient restClient;
    private final PersonajeRepository repositorio;

    public PersonajeService(RestClient rickAndMortyRestClient, PersonajeRepository repositorio) {
        this.restClient = rickAndMortyRestClient;
        this.repositorio = repositorio;
    }

    /**
     * Lista una pagina de personajes consultando siempre la API externa.
     * No persiste nada: el catalogo propio solo se llena al consultar por id.
     */
    public PaginaDto listarPagina(int pagina) {
        PaginaPersonajesApiDto respuesta = obtenerPaginaDeApi(pagina);

        List<PersonajeDto> personajes = respuesta.results().stream()
                .map(PersonajeDto::desdeApi)
                .toList();

        return new PaginaDto(
                pagina,
                respuesta.info().pages(),
                respuesta.info().count(),
                personajes);
    }

    /**
     * Devuelve un personaje buscandolo primero en la base de datos propia.
     * Si no esta guardado, lo pide a la API externa, lo guarda y lo devuelve.
     */
    @Transactional
    public PersonajeDto obtenerPorId(Long id) {
        return repositorio.findById(id)
                .map(PersonajeDto::desdeEntidad)
                .orElseGet(() -> guardarDesdeApi(id));
    }

    private PersonajeDto guardarDesdeApi(Long id) {
        PersonajeApiDto api = obtenerPersonajeDeApi(id);
        Personaje guardado = repositorio.save(aEntidad(api));
        return PersonajeDto.desdeEntidad(guardado);
    }

    private PaginaPersonajesApiDto obtenerPaginaDeApi(int pagina) {
        PaginaPersonajesApiDto respuesta;
        try {
            respuesta = restClient.get()
                    .uri("/character?page={pagina}", pagina)
                    .retrieve()
                    .body(PaginaPersonajesApiDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RecursoNoEncontradoException("No existe la pagina " + pagina);
        }
        if (respuesta == null || respuesta.info() == null || respuesta.results() == null) {
            throw new RestClientException("La API devolvio una respuesta vacia para la pagina " + pagina);
        }
        return respuesta;
    }

    private PersonajeApiDto obtenerPersonajeDeApi(Long id) {
        PersonajeApiDto respuesta;
        try {
            respuesta = restClient.get()
                    .uri("/character/{id}", id)
                    .retrieve()
                    .body(PersonajeApiDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RecursoNoEncontradoException("No existe un personaje con id " + id);
        }
        if (respuesta == null || respuesta.id() == null) {
            throw new RestClientException("La API devolvio una respuesta vacia para el personaje " + id);
        }
        return respuesta;
    }

    private static Personaje aEntidad(PersonajeApiDto api) {
        return new Personaje(
                api.id(),
                api.name(),
                api.species(),
                api.image(),
                api.origin() == null ? null : api.origin().name());
    }
}
