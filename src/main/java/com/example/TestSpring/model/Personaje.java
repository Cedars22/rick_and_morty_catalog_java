package com.example.TestSpring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * Personaje guardado en la base de datos propia.
 * Solo se persisten los cinco campos que interesan al catalogo.
 */
@Entity
@Table(name = "personajes")
public class Personaje {

    /** Id que asigna la API externa: no se genera localmente. */
    @Id
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String especie;

    @Column(length = 512)
    private String imagen;

    private String origen;

    protected Personaje() {
        // Constructor sin argumentos requerido por JPA.
    }

    public Personaje(Long id, String nombre, String especie, String imagen, String origen) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.imagen = imagen;
        this.origen = origen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Personaje personaje)) {
            return false;
        }
        return id != null && id.equals(personaje.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
