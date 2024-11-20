package com.aluracursos.desafiodepracticaspringboot.molde;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosResultado(@JsonAlias("results")List<DatosLibro> resultado) {
}
