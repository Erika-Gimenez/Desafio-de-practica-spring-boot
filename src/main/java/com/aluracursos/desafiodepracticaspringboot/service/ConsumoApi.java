package com.aluracursos.desafiodepracticaspringboot.service;

import com.aluracursos.desafiodepracticaspringboot.excepcion.ErrorEnPeticionNullEspacioEnBlancoException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {

    public String obtenDatos(String url){

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        String json = response.body();

//        if (json == null || json.isBlank()){
//            throw new ErrorEnPeticionNullEspacioEnBlancoException("El cuerpo de la respuesta está vacío o contiene datos nulos.");
//        }
        return json;
    }

}
