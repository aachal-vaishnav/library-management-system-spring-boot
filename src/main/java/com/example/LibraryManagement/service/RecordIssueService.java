package com.example.LibraryManagement.service;

import com.example.LibraryManagement.entity.Book;
import com.example.LibraryManagement.entity.RecordIssued;
import com.example.LibraryManagement.entity.User;
import com.example.LibraryManagement.repository.BookRepository;
import com.example.LibraryManagement.repository.RecordIssuedRepository;
import com.example.LibraryManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RecordIssueService {
    @Autowired
    private RecordIssuedRepository recordIssuedRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public RecordIssued issueTheBook(Long bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new RuntimeException("Book not found for id "+bookId));
        if(book.getQuantity() <= 0 || !book.getIsAvailable()){
            throw new RuntimeException("Book is not Available");
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User Not Found!"));

        RecordIssued recordIssued = new RecordIssued();
        recordIssued.setIssueDate(LocalDate.now());
        recordIssued.setDueDate(LocalDate.now().plusDays(14));
        recordIssued.setIsReturned(false);
        recordIssued.setUser(user);
        recordIssued.setBook(book);

        book.setQuantity(book.getQuantity()-1);
        if(book.getQuantity() == 0){
            book.setIsAvailable(false);
        }

        bookRepository.save(book);
        return recordIssuedRepository.save(recordIssued);
    }
    public RecordIssued returnTheBook(Long issueRecordId){
        RecordIssued recordIssued = recordIssuedRepository.findById(issueRecordId).
                orElseThrow(()-> new RuntimeException("Issue Record not Found!"));
        if(recordIssued.getIsReturned()){
            throw  new RuntimeException("Book is already returned!");
        }
        Book book = recordIssued.getBook();
        book.setQuantity(book.getQuantity()+1);
        book.setIsAvailable(true);
        bookRepository.save(book);

        recordIssued.setReturnDate(LocalDate.now());
        recordIssued.setIsReturned(true);

        return recordIssuedRepository.save(recordIssued);
    }
}
