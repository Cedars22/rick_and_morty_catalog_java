package com.example.testspring.exception;

/** El recurso pedido no existe ni en la base de datos ni en la API externa. */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
