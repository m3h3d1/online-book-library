package com.mehedi.service;

import com.mehedi.constatnts.AvailabilityStatus;
import com.mehedi.constatnts.ReservationStatus;
import com.mehedi.entity.Book;
import com.mehedi.entity.BookReservation;
import com.mehedi.entity.User;
import com.mehedi.exception.*;
import com.mehedi.repository.BookRepository;
import com.mehedi.repository.BookReservationRepository;
import com.mehedi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookReservationService {
    private final BookReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookReservationService(BookReservationRepository reservationRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public void reserveBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Check if the book is currently available
        if (book.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
            // Book is available, no need to reserve
            return;
        }

        // Check if the user has already reserved the same book
        List<BookReservation> existingReservations = reservationRepository.findByUserAndBook(user, book);
        if (!existingReservations.isEmpty()) {
            // User already reserved this book
            return;
        }

        // Create a new reservation entry
        BookReservation reservation = new BookReservation();
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setReservationDate(LocalDate.now());
        reservation.setNotified(false);
        reservation.setReservationStatus(ReservationStatus.PENDING);

        reservationRepository.save(reservation);
    }
}
