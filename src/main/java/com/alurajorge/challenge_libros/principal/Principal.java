package com.alurajorge.challenge_libros.principal;

import com.alurajorge.challenge_libros.model.*;
import com.alurajorge.challenge_libros.repository.AutorRepository;
import com.alurajorge.challenge_libros.repository.LibrosRepository;
import com.alurajorge.challenge_libros.service.ConsumoAPI;
import com.alurajorge.challenge_libros.service.ConvertirDatos;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

public class Principal {
    private Scanner teclado=new Scanner(System.in);
    ConsumoAPI consumoAPI=new ConsumoAPI();
    private final String URL_BASE="http://gutendex.com/books/";
    private final String API_KEY="&apikey=14caddfc";

    private ConvertirDatos conversor=new ConvertirDatos();

    private LibrosRepository librosRepository;
    private AutorRepository autorRepository;

    List<Libros> libros;
    List<Autor> autores;

    public Principal(LibrosRepository librosRepository, AutorRepository autorRepository) {

        this.librosRepository=librosRepository;
        this.autorRepository=autorRepository;
    }

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            var menu = """
                 ************* Elija la opcion a travez de su numero **********   
                            1 - Buscar Libro por titulo
                            2 - Mostrar los libros Registrados
                            3 - Mostrar los Autores Registrados
                            4 - Listar autores vivos en un determinado año
                            5 - Listar libros por idioma
                            6 - Buscar Autor por nombre
                            7 - Buscar Autores por una fecha nacimiento determinada
                            8 - Mostrar Top 10 de Libros con Mayor Descargas en la aplicacion
                            9 - Mostrar Top 10 de Libros con Mayor Descargas en la API
                            10 - Datos Estadisticos de libros
                            0 - Salir
                 **************************************************************""";
            System.out.println(menu);
            try {
                opcion = Integer.valueOf(teclado.nextLine());

                switch (opcion) {
                    case 1:
                        getDatosLibros();
                        break;
                    case 2:
                        mostrarLibrosRegistrados();
                        break;
                    case 3:
                        mostrarAutoresRegistrados();
                        break;
                    case 4:
                        mostrarAutoresVivosPorFecha();
                        break;
                    case 5:
                        mostrarLibrosPorIdioma();
                        break;
                    case 6:
                        mostrarAutorPorNombre();
                        break;
                    case 7:
                        mostrarAutorPorFechaNacimiento();
                        break;
                    case 8:
                        mostrarTop10DeLibroConMayorDescargas();
                        break;
                    case 9:
                        mostrarTop10DeLibrosEnLaAPI();
                        break;
                    case 10:
                        datosEstadisticosLibros();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            }catch (NumberFormatException e){
                System.out.println("La opcion no es valida, Ingrese un numero.");
            }
        }

    }

    public void getDatosLibros(){
        System.out.println("Ingrese el nombre del libro que desea buscar");
        String tituloLibro=teclado.nextLine();
        boolean esNumero=esEntero(tituloLibro);
        if(!esNumero) {
            var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
            Datos libroBusquedo = conversor.obtenerDatos(json, Datos.class);

            Optional<Libros> libroBuscado = libroBusquedo.libros().stream()
                    .filter(t -> t.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                    .findFirst()
                    .map(l -> new Libros(l));

            if (libroBuscado.isPresent()) {
                Libros libro = libroBuscado.get();

                if (libro.getAutor() != null) {
                    Autor autor = autorRepository.findAutorByNombre(libro.getAutor().getNombre());

                    if (autor == null) {
                        Autor nuevoAutor = libro.getAutor();
                        autor = autorRepository.save(nuevoAutor);
                    }

                    try {
                        libro.setAutor(autor);
                        librosRepository.save(libro);
                        System.out.println(libro);
                    } catch (DataIntegrityViolationException e) {
                        System.out.println("El libro ya se encuentra registrado en la base de datos.");
                    }
                }
            } else {
                System.out.println("No se encontró el libro de titulo: " + tituloLibro);
            }
        }else{
            System.out.println("Ingrese un nombre de libro");
        }
    }


//******************************** mostrar libros registrados ******************************
    public void mostrarLibrosRegistrados(){
            libros = librosRepository.findAll();
            libros.stream()
                    .forEach(System.out::println);
    }
    public void mostrarAutoresRegistrados(){
         autores= autorRepository.findAll();
         autores.forEach(System.out::println);
    }

    public void mostrarAutoresVivosPorFecha(){
        System.out.println("Ingrese el año, para saber que Autores se encontraban vivos hasta esa fecha");
        try{
            int agno = teclado.nextInt();
            List<Autor> autore = autorRepository.autoresVivosPorFecha(agno);
            if(autore.isEmpty()){
                System.out.println("No se encontraron registros de autores vivos durante ese año en la base de datos.");
            }else{
                autore.forEach(System.out::println);
            }

        }catch (InputMismatchException e){
            System.out.println("Debes ingresar un año válido.");
        }

    }

    public void mostrarLibrosPorIdioma(){
        System.out.println("Ingrese el idioma, para mostrar los libros que se encuentran es ese Idioma: ");
        String idioma = teclado.nextLine();
        String jsonString = "{\"es\":\"Español\", \"en\":\"Ingles\",\"fr\":\"Frances\",\"pt\":\"Portuges\" }";
        JSONObject paises=new JSONObject(jsonString);
        Iterator<String> claves = paises.keys();
        while (claves.hasNext()) {
            String clave = claves.next();
            String valor = paises.getString(clave);

            if (valor.toUpperCase().equals(idioma.toUpperCase())){
                List<Libros> librosAutores = librosRepository.findLibrosByIdioma(clave.toUpperCase());
                if(librosAutores.isEmpty()){
                    System.out.println("No se encontraron libros en ese idioma");
                }else{
                    librosAutores.forEach(System.out::println);
                }
            }

        }
    }

    public void datosEstadisticosLibros(){
        libros=librosRepository.findAll();
        DoubleSummaryStatistics est= libros.stream()
                .filter(e -> e.getNumeroDescargas()>0)
                .collect(Collectors.summarizingDouble(Libros::getNumeroDescargas));

        System.out.println("La media de las descargas :"+ est.getAverage());
        System.out.println("El libro con mayor descargas : " + est.getMax());
        System.out.println("El libro con menos descargas :"+est.getMin());

    }

    public void mostrarAutorPorNombre(){
        System.out.println("Escribe el nombre del autor que deseas buscar");
        String nombreAutor=teclado.nextLine();
        Autor datosAu=autorRepository.mostrarAutorPorNombre(nombreAutor.toUpperCase());
        if (datosAu !=null){
            System.out.println(datosAu);
        }else{
            System.out.println("Autor no se encuentra registrado en nuestra base de datos");
        }
    }

    public void mostrarTop10DeLibroConMayorDescargas(){
        libros = librosRepository.findTop10ByOrderByNumeroDescargasDesc();
        libros.forEach(System.out::println);
    }

    //********* mostrar autores por una fecha de nacimientos
    public void mostrarAutorPorFechaNacimiento(){
        try {
            System.out.println("ingrese una año, para ver los autores nacidos a partir de esa año");
            int fecha = Integer.valueOf(teclado.nextLine());
            autores = autorRepository.mostrarAutorPorFechaNacimiento(fecha);
            autores.forEach(System.out::println);
        }catch (NumberFormatException e){
            System.out.println("Ingrese una fecha, y no letras");
        }
    }

    //************* metodo que ayuda ha saber si un String ingresado es un numero retornando un boolean
    public static boolean esEntero(String entrada) {
        try {
            Integer.parseInt(entrada);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void mostrarTop10DeLibrosEnLaAPI(){
        var json=consumoAPI.obtenerDatos(URL_BASE);
        Datos datos=conversor.obtenerDatos(json,Datos.class);

        datos.libros().stream().sorted(Comparator.comparing(DatosLibros::numeroDescargas).reversed())
                .limit(10)
                .forEach(System.out::println);
    }


}
