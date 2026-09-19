package com.example.TestSpring.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PersonajeTest {

    private static Personaje rick() {
        return new Personaje(1L, "Rick Sanchez", "Human", "imagen.jpeg", "Earth (C-137)");
    }

    @Test
    void exponeLosCamposDelConstructor() {
        Personaje personaje = rick();

        assertThat(personaje.getId()).isEqualTo(1L);
        assertThat(personaje.getNombre()).isEqualTo("Rick Sanchez");
        assertThat(personaje.getEspecie()).isEqualTo("Human");
        assertThat(personaje.getImagen()).isEqualTo("imagen.jpeg");
        assertThat(personaje.getOrigen()).isEqualTo("Earth (C-137)");
    }

    @Test
    void permiteModificarLosCampos() {
        Personaje personaje = rick();

        personaje.setId(2L);
        personaje.setNombre("Morty Smith");
        personaje.setEspecie("Alien");
        personaje.setImagen("otra.jpeg");
        personaje.setOrigen("unknown");

        assertThat(personaje.getId()).isEqualTo(2L);
        assertThat(personaje.getNombre()).isEqualTo("Morty Smith");
        assertThat(personaje.getEspecie()).isEqualTo("Alien");
        assertThat(personaje.getImagen()).isEqualTo("otra.jpeg");
        assertThat(personaje.getOrigen()).isEqualTo("unknown");
    }

    @Test
    void dosPersonajesConElMismoIdSonIguales() {
        Personaje unRick = rick();
        Personaje otroRick = new Personaje(1L, "Rick de otra dimension", "Human", "x.jpeg", "Earth (C-500A)");

        assertThat(unRick)
                .isEqualTo(otroRick)
                .hasSameHashCodeAs(otroRick);
    }

    @Test
    void personajesConDistintoIdNoSonIguales() {
        assertThat(rick()).isNotEqualTo(
                new Personaje(2L, "Morty Smith", "Human", "y.jpeg", "Earth (C-137)"));
    }

    @Test
    void cumpleElContratoDeEquals() {
        Personaje personaje = rick();

        assertThat(personaje.equals(personaje)).isTrue();
        assertThat(personaje.equals(null)).isFalse();
        assertThat(personaje.equals("Rick Sanchez")).isFalse();
    }

    @Test
    void unPersonajeSinIdNoEsIgualAOtroSinId() {
        assertThat(new Personaje(null, "A", "B", "C", "D"))
                .isNotEqualTo(new Personaje(null, "A", "B", "C", "D"));
    }
}
