package com.mehedi.service;
import com.mehedi.constatnts.AvailabilityStatus;
import com.mehedi.entity.Book;
import com.mehedi.entity.User;
import com.mehedi.exception.*;
import com.mehedi.repository.BookRepository;
import com.mehedi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public Book createBook(Book book) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();

        if(!userNow.getRole().equals(User.Role.ADMIN)) {
            throw new UnauthorizedUserException("You are not Authorized");
        }

        if (book.getTitle() == "" || book.getAuthor() == "") {
            throw new IllegalArgumentException("Title and author cannot be null.");
        }

        if(bookRepository.existsByTitleAndAuthor(book.getTitle(), book.getAuthor())) {
            throw new DuplicateBookException("A book with the same title and author already exists.");
        }

        book.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        return bookRepository.save(book);
    }

    public Book updateBook(Long bookId, Book updatedBook) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();

        if(!userNow.getRole().equals(User.Role.ADMIN)) {
            throw new UnauthorizedUserException("You are not Authorized");
        }

        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
//        existingBook.setDueDate(updatedBook.getDueDate());

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();

        if(!userNow.getRole().equals(User.Role.ADMIN)) {
            throw new UnauthorizedUserException("You are not Authorized");
        }

        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
        bookRepository.delete(existingBook);
    }

    public List<Book> getAllBooks() {
        try {
            return bookRepository.findAll();
        } catch (Exception ex) {
            throw new BookServiceException("Failed to fetch books.", ex);
        }
    }

    public Optional<Book> findBookById(Long bookId) {
        return bookRepository.findById(bookId);
    }
}
