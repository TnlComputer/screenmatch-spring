package com.aluracursos.screenmatch.service;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json, clase);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }
}
