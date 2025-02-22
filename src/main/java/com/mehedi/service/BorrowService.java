package com.mehedi.service;

import com.mehedi.constatnts.AvailabilityStatus;
import com.mehedi.dto.BorrowRequest;
import com.mehedi.entity.Book;
import com.mehedi.entity.BookBorrow;
import com.mehedi.entity.User;
import com.mehedi.exception.*;
import com.mehedi.repository.BookBorrowRepository;
import com.mehedi.repository.BookRepository;
import com.mehedi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Date;

@Service
public class BorrowService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookBorrowRepository bookBorrowRepository;

    @Transactional
    public void borrowBook(Long bookId, Long userId, LocalDate dueDate) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        if (book.getAvailabilityStatus() == AvailabilityStatus.BORROWED) {
            throw new BookUnavailableException("Book is already borrowed.");
        }

        // Set the book's availability status to BORROWED.
        book.setAvailabilityStatus(AvailabilityStatus.BORROWED);

        // Create a new BookBorrow entry.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        BookBorrow bookBorrow = new BookBorrow();
        bookBorrow.setUser(user);
        bookBorrow.setBook(book);
        bookBorrow.setBorrowDate(LocalDate.now());
        bookBorrow.setDueDate(dueDate);
        bookBorrow.setReturnDate(null);

        // Save the updated book and the new bookBorrow entry to the database.
        bookRepository.save(book);
        bookBorrowRepository.save(bookBorrow);
    }

//    public void borrowBook(Long bookId, Long userId, LocalDate borrowDate, LocalDate dueDate) {
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
//
//        if (book.getAvailabilityStatus() == AvailabilityStatus.BORROWED) {
//            throw new BookUnavailableException("Book is already borrowed.");
//        }
//
//        // Set the book's availability status to BORROWED.
//        book.setAvailabilityStatus(AvailabilityStatus.BORROWED);
//
//        // Set the due date based on the provided parameter.
//        book.setBorrowDate(borrowDate);
//        book.setDueDate(dueDate);
//
//        // Associate the book with the user by updating the book's user_id field.
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
//        book.setUser(user);
//
//        // Save the updated book to the database.
//        bookRepository.save(book);
//    }

//    public void returnBook(Long bookId) {
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
//
//        if (book.getAvailabilityStatus() != AvailabilityStatus.BORROWED) {
//            throw new ErrorMessage("Book is not currently borrowed.");
//        }
//
//        // Update the book's availability status to AVAILABLE.
//        book.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
//
//        // Remove the user association by setting the book's user to null.
//        book.setUser(null);
//
//        // Save the updated book to the database.
//        bookRepository.save(book);
//    }


    public void returnBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        if (!book.getAvailabilityStatus().equals(AvailabilityStatus.BORROWED)) {
            throw new BookReturnException("Book is not borrowed by the user.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Find the corresponding BookBorrow entry for this book and user
        BookBorrow bookBorrow = bookBorrowRepository.findByBookAndUser(book, user);

        if (bookBorrow == null) {
            throw new BookReturnException("No borrow record found for this user and book.");
        }

        // Set the book's availability status to AVAILABLE.
        book.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        // Remove the association between the book and the user.
        book.setUser(null);

        // Set the return date to the current date.
        bookBorrow.setReturnDate(LocalDate.now());

        // Save the updated book and the updated bookBorrow entry to the database.
        bookRepository.save(book);
        bookBorrowRepository.save(bookBorrow);
    }


//    public void returnBook(Long bookId, Long userId) {
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
//
//        if (!book.getAvailabilityStatus().equals(AvailabilityStatus.BORROWED)) {
//            throw new BookReturnException("Book is not borrowed by the user.");
//        }
//
//        // Set the book's availability status to AVAILABLE.
//        book.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
//
//        // Remove the association between the book and the user.
////        book.setUser(null);
//
//        book.setDueDate(null);
//
//        // Save the updated book to the database.
//        bookRepository.save(book);
//    }
}
