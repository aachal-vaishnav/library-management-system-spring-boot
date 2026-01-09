package com.example.LibraryManagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class RecordIssued {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Boolean isReturned;

    //one user can issue many book
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //one book can have many issue records
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
