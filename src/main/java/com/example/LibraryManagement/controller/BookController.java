package com.example.LibraryManagement.controller;

import com.example.LibraryManagement.dto.BookDTO;
import com.example.LibraryManagement.entity.Book;
import com.example.LibraryManagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/getallbooks")
    public ResponseEntity<List<Book>> getAllBook(){
        return ResponseEntity.ok(bookService.getAllBook());
    }

    @GetMapping("/getbooksbyid/{id}")
    public ResponseEntity<Book> getbookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping("/addbook")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> addBook(@RequestBody BookDTO bookDTO){
        return ResponseEntity.ok(bookService.addBook(bookDTO));
    }

    @PutMapping("/updatebook")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> updateBook(@PathVariable Long id,@RequestBody BookDTO bookDTO){
        return ResponseEntity.ok(bookService.updateBook(id,bookDTO));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id){
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }


}
