package com.mehedi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "availability_status")
    private String availabilityStatus; // Use enum or string, depending on your preference

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate; // Use java.util.Date or java.time.LocalDate, as needed
}