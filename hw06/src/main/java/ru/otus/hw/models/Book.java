package ru.otus.hw.models;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "books_genres", joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Genre> genres;
}
