package com.aluracursos.screenmatch.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Brenda", "Luis", "Maria", "Eric", "Genesis");

        nombres.stream()
                .sorted()
                .limit(4) // limita la cantidad de nombres a mostrar
                .filter(n -> n.startsWith("L"))  // Filter: permite filtrar
                // los elementos del stream basándose en una condición.
                .map(n -> n.toUpperCase()) // Map: permite transformar cada
                // elemento del stream en otro tipo de dato.
                .forEach(System.out::println); // ForEach: permite ejecutar una acción
                // en cada elemento de la stream. Por ejemplo, podemos imprimir cada elemento
                // de la lista.

    }
}
