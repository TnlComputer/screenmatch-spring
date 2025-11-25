package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jdk.jfr.Category;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosSerie(
//      @JsonAlias lo usamos para mapear el nombre de la api con nuestra app
        @JsonAlias("Title") String titulo,
        @JsonAlias("totalSeasons") Integer totalDeTemporadas,
        @JsonAlias("imdbRating") String evaluacion,
        @JsonAlias("Genre") String genero,
        @JsonAlias("Actors") String actores,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Plot") String sinopsis
//        @JsonProperty("nombreEnJson")
//        Esta anotación se utiliza para definir el nombre de la propiedad JSON
//        asociada al campo Java./Por ejemplo, si tienes una clase Java con la
//        propiedad "nombreCompleto" y deseas que se mapee al JSON como
//        "nombre", puedes usar @JsonProperty("nombre") para especificar el
//        nombre correcto en el JSON:
        ) {
}
