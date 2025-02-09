package com.example.literatura.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String title;
    private String author;
    private String language;
    private Double downloads;

    public Libro() {
    }

    public Libro(LibroData bookData) {
        this.title = bookData.title();
        this.author = getFirstAuthor(bookData).getName();
        this.language = getFirstLanguage(bookData);
        this.downloads = bookData.downloads();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getDownloads() {
        return downloads;
    }

    public void setDownloads(Double downloads) {
        this.downloads = downloads;
    }

    public Autor getFirstAuthor(LibroData bookData) {
        AutorData authorData = bookData.author().getFirst();
        return new Autor(authorData);
    }

    public String getFirstLanguage(LibroData bookData) {
        return bookData.language().get(0);
    }

    @Override
    public String toString() {
        return "**** Información del libro ****" +
                "\n\tTitle: " + title +
                "\n\tAuthor: " + author +
                "\n\tLanguage: " + language +
                "\n\tDownloads: " + downloads;
    }
}

