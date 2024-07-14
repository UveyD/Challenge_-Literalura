package com.example.literatura.Main;

import com.example.literatura.Model.Autor;
import com.example.literatura.Model.Libro;
import com.example.literatura.Model.LibroData;
import com.example.literatura.Model.Results;
import com.example.literatura.Repository.AutorRepository;
import com.example.literatura.Repository.LibroRepository;
import com.example.literatura.Service.ConsumptionAPI;
import com.example.literatura.Service.ConvertData;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private Scanner sc = new Scanner(System.in);
    private ConvertData convertData = new ConvertData();
    private ConsumptionAPI consumptionApi = new ConsumptionAPI();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    List<Libro> books;
    List<Autor> authors;

    public Main(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void showMenu() {
        final var menu = """
                \n\t**** Por favor, seleccione una opción ****
                \t1 - Buscar un libro por título
                \t2 - Lista de libros registrados
                \t3 - Lista de autores registrados
                \t4 - Lista de autores vivos en un año 
                \t5 - Lista de libros por lenguaje
                \n\t0 - Exit/Salir
                """;
        var opcion = -1;
        System.out.println("****************************************");
        while (opcion != 0) {
            System.out.println(menu);
            System.out.print("Opcion: ");
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                    buscaLibroPorTitulo();
                    break;
                case 2:
                    ListaDeLibrosRegistrados();
                    break;
                case 3:
                    listaDeAutoresRegistrados();
                    break;
                case 4:
                    listaDeAutoresVivosEnUnAÑO();
                    break;
                case 5:
                    listaDeLibrosPorLenguaje();
                    break;
                case 0:
                    System.out.println("ending application...");
                    break;
                default:
                    System.out.println("Invalida Opción, por favor, intenta de nuevo ");
                    break;
            }
        }
        System.out.println("****************************************");
    }

    private void buscaLibroPorTitulo() {
        System.out.print("Buscar libro por titulo...Por favor, escriba el libro: ");
        String inTitle = sc.nextLine();
        var json = consumptionApi.getData(inTitle.replace(" ", "%20"));
        //System.out.println("json: " + json);
        var data = convertData.getData(json, Results.class);
        //System.out.println("data: " + data);
        if (data.results().isEmpty()) {
            System.out.println("No se encontro el libro, lo siento");
        } else {
            LibroData bookData = data.results().getFirst();
            //System.out.println("bookData: " + bookData);
            Libro book = new Libro(bookData);
            //System.out.println("book: " + book);
            Autor author = new Autor().getFirstAuthor(bookData);
            //System.out.println("author: " + author);
            saveData(book, author);
        }
    }

    private void saveData(Libro book, Autor author) {
        Optional<Libro> bookFound = libroRepository.findByTitleContains(book.getTitle());
        //System.out.println("bookFound: " + bookFound);
        if (bookFound.isPresent()) {
            System.out.println("¡Este libro, ya se encuentra registrado!");
        } else {
            try {
                libroRepository.save(book);
                System.out.println("¡Libro registrado con exito!");
            } catch (Exception e) {
                System.out.println("Error message: " + e.getMessage());
            }
        }

        Optional<Autor> authorFound = autorRepository.findByNameContains(author.getName());
        //System.out.println("authorFound: " + authorFound);
        if (authorFound.isPresent()) {
            System.out.println("Este autor ya se encuentra registrado");
        } else {
            try {
                autorRepository.save(author);
                System.out.println("Autor registrado");
            } catch (Exception e) {
                System.out.println("Error message: " + e.getMessage());
            }
        }
    }

    private void ListaDeLibrosRegistrados() {
        System.out.println("Lista de libros registrados\n---------------------");
        books = libroRepository.findAll();
        books.stream()
                .sorted(Comparator.comparing(Libro::getTitle))
                .forEach(System.out::println);
    }

    private void listaDeAutoresRegistrados() {
        System.out.println("Lista de autores registrados\n-----------------------");
        authors = autorRepository.findAll();
        authors.stream()
                .sorted(Comparator.comparing(Autor::getName))
                .forEach(System.out::println);
    }

    private void listaDeAutoresVivosEnUnAÑO() {
        System.out.print("Lista de autores vivos en un año...Por favor, introduce el año: ");
        Integer year = Integer.valueOf(sc.nextLine());
        authors = autorRepository
                .findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
        if (authors.isEmpty()) {
            System.out.println("Authors alive not found");
        } else {
            authors.stream()
                    .sorted(Comparator.comparing(Autor::getName))
                    .forEach(System.out::println);
        }
    }

    private void listaDeLibrosPorLenguaje() {
        System.out.println("Lista de libros por lenguaje\n----------------------");
        System.out.println("""
                \n\t---- Por favor, indique el lenguaje ----
                \ten - English-Ingles
                \tes - Spanish-Español
                \tfr - French-France
                \tpt - Portuguese-Portuges
                """);
        String lang = sc.nextLine();
        books = libroRepository.findByLanguageContains(lang);
        if (books.isEmpty()) {
            System.out.println("Libros por language seleccionado no encontrado");
        } else {
            books.stream()
                    .sorted(Comparator.comparing(Libro::getTitle))
                    .forEach(System.out::println);
        }
    }
}
