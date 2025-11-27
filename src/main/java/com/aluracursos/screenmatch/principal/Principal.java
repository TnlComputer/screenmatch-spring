package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private static final String URL_BASE = "https://omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=774ea8f6";
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;


    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    1 - Buscar series
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar series por tìtulo
                    5 - Top 5 Mejores series
                    6 - Buscar Series por Categoría
                    7 - Filtrar series
                    ___________________________
                    0 - Salir
                    
                    Ingrese una opcion.
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    mostrarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    //  busca los datos generales de las series
    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    //  busca los datos de toda la temporada
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie, de la cual quieras ver los episodios: ");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();

            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalDeTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&Season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }
    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void mostrarSeriesPorTitulo(){
        System.out.println("Escribe el titulo de la serie que deseas buscar: ");
        var nombreSerie = teclado.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()){
            System.out.println("La serie buscada es: " + serieBuscada.get());
        }else {
            System.out.println("Serie no encontrada");
        }

    }

    private void buscarTop5Series(){
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + " Evaluacion: " + s.getEvaluacion()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Escriba el bénero o categoría que desea buscar: ");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriePorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoría " + genero);
        seriePorCategoria.forEach(System.out::println);
    }

    public void filtrarSeriesPorTemporadaYEvaluacion(){
        System.out.println("¿Filtrar séries con cuántas temporadas? ");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Como evaluación a partir de cuál valor? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
//        List<Serie> filtroSeries = repositorio.findByTotalDeTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(totalTemporadas, evaluacion);
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaYEvaluacion(totalTemporadas, evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }
}


//        Mostrar solo el título de los episodios para las temporadas
//        for (int i = 0; i < datos.totalDeTemporadas() ; i++) {
//            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
//      La función Lambda (función anónima) reemplaza las 4 líneas de arriba en solo una.
        //temporadas.forEach(t-> t.episodios().forEach(e -> System.out.println("FE "+ e.titulo())));

        // convertir todas las informaciones a una lista del tipo DatosEpisodio

//        List<DatosEpisodio> datosEpisodios = temporadas.stream()
//                .flatMap(t ->t.episodios().stream())
//                .toList();
                //.collect(Collectors.toList());

        // Top 5 Episodios
//        System.out.println("Top 5 Episodios");
//        datosEpisodios.stream()
//                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
//                // La función "peek" es una operación intermedia de una stream. Una operación intermedia es aquella que
//                // procesa los datos del stream, pero no la finaliza, es decir, permite que se realicen más operaciones
//                // después de ella. La función peek se utiliza para "espiar" los elementos del stream sin alterarlos,
//                // lo que puede ser muy útil para la depuración. Esta función recibirá cada elemento del stream y
//                // realizará alguna operación con efecto secundario (como imprimir el elemento), pero devolverá el
//                // mismo elemento sin cambiar nada.
//                .peek(e -> System.out.println("Primer Filtro (N/A) " + e))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(e -> System.out.println("Segundo Filtro (M<m) " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Tercer Filtro (m>M) " + e))
//                .limit(5)
//                .forEach(System.out::println);

        // convirtiendo los datos a una lista del tipo episodio
//        List<Episodio> episodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream()
//                        .map(d -> new Episodio(t.numero(),d)))
////                .toList();
//                .collect(Collectors.toList());

        //episodios.forEach(System.out::println);


        // Busqueda de episodios a partir de X año
//        System.out.println(("Por favor indica el año a partir del cual deseas ver los episodios"));
//        var fecha = teclado.nextInt();
//
//        LocalDate fechaBusqueda = LocalDate.of(fecha,1,1);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println(
//                        "Temporada " + e.getTemporada() +
//                                "Episodio " + e.getTitulo() +
//                                "Fecha de Lanzamiento " + e.getFechaDeLanzamiento().format(dtf)
//                ));

        // Busqueda de episodios por parte del título

//        System.out.println("Por favor introduce el título que deseas ver");
//        var parteTitulo = teclado.nextLine();
//        System.out.println("Buscando..." + parteTitulo);
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(parteTitulo.toUpperCase()))
//                .findFirst();
//        if(episodioBuscado.isPresent()) {
//            System.out.println("Episodio encontrado");
//            System.out.println("Los datos son: " + episodioBuscado.get());
//        }else{
//            System.out.println("Episodio no encontrado");
//        }
//        Map<Integer , Double> evaluacionesPorTemporada = episodios.stream()
//                .filter(e -> e.getEvaluacion() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada,
//                        Collectors.averagingDouble(Episodio::getEvaluacion)));
//        System.out.println(evaluacionesPorTemporada);
//        DoubleSummaryStatistics est = episodios.stream()
//                .filter(e -> e.getEvaluacion() > 0.0)
//                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
//        System.out.println("Media: " + est.getAverage());
//        System.out.println("Max: " + est.getMax());
//        System.out.println("Media: " + est.getMin());
//    }
