package com.alurajorge.challenge_libros.service;

public interface IConvertirDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
