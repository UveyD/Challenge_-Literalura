package com.example.literatura.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "authors")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Libro> books;

    public Autor() {
    }

    public Autor(AutorData authorData) {
        this.name = authorData.name();
        this.birthYear = Integer.valueOf(authorData.birthYear());
        this.deathYear = Integer.valueOf(authorData.deathYear());
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirth_year() {
        return birthYear;
    }

    public void setBirth_year(Integer birth_year) {
        this.birthYear = birth_year;
    }

    public Integer getDeath_year() {
        return deathYear;
    }

    public void setDeath_year(Integer death_year) {
        this.deathYear = death_year;
    }

    public List<Libro> getBooks() {
        return books;
    }

    public void setBooks(List<Libro> books) {
        this.books = books;
    }

    public Autor getFirstAuthor(LibroData bookData) {
        AutorData authorData = bookData.author().getFirst();
        return new Autor(authorData);
    }

    @Override
    public String toString() {
        return "**** Información del autor ****" +
                "\n\tName: " + name +
                "\n\tBirth year: " + birthYear +
                "\n\tDeath year: " + deathYear;
    }
}

