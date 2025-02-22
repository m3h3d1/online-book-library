package com.mehedi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mehedi.constatnts.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
//    private String availabilityStatus; // Use enum or string, depending on your preference
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

//    @Column(name = "borrow_date")
//    @Temporal(TemporalType.DATE)
//    private LocalDate borrowDate;

//    @Column(name = "due_date")
//    @Temporal(TemporalType.DATE)
//    private LocalDate dueDate; // Use java.util.Date or java.time.LocalDate, as needed

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id") // Name of the foreign key column in the Book table
//    @JsonIgnore
//    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

//    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<BookReview> reviews = new ArrayList<>();
}