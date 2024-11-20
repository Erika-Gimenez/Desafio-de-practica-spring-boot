package com.aluracursos.desafiodepracticaspringboot.service;

public interface IDeserializacionDatosApi {

    <T> T obtenerDatos(String json, Class<T> clase);

}
