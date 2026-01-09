package com.example.LibraryManagement.service;

import com.example.LibraryManagement.dto.BookDTO;
import com.example.LibraryManagement.entity.Book;
import com.example.LibraryManagement.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    public List<Book> getAllBook(){
        return bookRepository.findAll();
    }

    public Book getBookById(Long id){
        Book book = bookRepository.findById(id).orElseThrow(()-> new RuntimeException("Book not Found with id "+id));
        return book;
    }

    public Book  addBook(BookDTO bookDTO){
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setIsAvailable(bookDTO.getIsAvailable());
        book.setQuantity(bookDTO.getQuantity());

        return bookRepository.save(book);
    }
    public  Book updateBook(Long id, BookDTO bookDTO){
        Book oldBook = bookRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Book Not Found"));
        oldBook.setTitle(bookDTO.getTitle());
        oldBook.setAuthor(bookDTO.getAuthor());
        oldBook.setIsbn(bookDTO.getIsbn());
        oldBook.setIsAvailable(bookDTO.getIsAvailable());
        oldBook.setQuantity(bookDTO.getQuantity());


        return bookRepository.save(oldBook);
    }

    public void deleteBookById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

}
