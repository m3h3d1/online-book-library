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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public List<BookReservation> getReservationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return reservationRepository.findByUser(user);
    }

    public List<BookReservation> getReservationsByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        return reservationRepository.findByBook(book);
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

//    public void notifyUserForAvailableBook(Long bookId) {
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
//
//        // Get all reservations for this book
//        List<BookReservation> reservations = reservationRepository.findByBook(book);
//
//        // Check each reservation and notify users if the book is now available
//        for (BookReservation reservation : reservations) {
//            if (!reservation.isNotified()) {
//                // Notify the user (you can implement notification logic here)
//                reservation.setNotified(true);
//                reservationRepository.save(reservation);
//            }
//        }
//    }

//    public void cancelReservation(Long reservationId, Long userId) {
//        BookReservation reservation = reservationRepository.findById(reservationId)
//                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with id: " + reservationId));
//
//        if (!reservation.getUser().getUserId().equals(userId)) {
//            throw new UnauthorizedUserException("You are not authorized to cancel this reservation.");
//        }
//
//        if (reservation.getReservationStatus() == ReservationStatus.CANCELED) {
//            throw new BookReservationException("The reservation is already canceled.");
//        }
//
//        reservation.setReservationStatus(ReservationStatus.CANCELED);
//        reservationRepository.save(reservation);
//    }

//    public void cancelReservation(Long bookId, Long userId) {
//        // Find the reservation using Optional
//        Optional<BookReservation> optionalReservation = reservationRepository.findByBookIdAndUserId(bookId, userId);
//
//        // Check if the reservation exists
//        if (optionalReservation.isPresent()) {
//            // Reservation found, proceed to delete it
//            reservationRepository.delete(optionalReservation.get());
//        } else {
//            // Reservation not found, throw an exception
//            throw new ReservationNotFoundException("Reservation not found for bookId: " + bookId);
//        }
//    }

//    public void cancelReservation(Long bookId, Long userId) {
//        // Check if the reservation exists for the given book and user
//        BookReservation reservation = reservationRepository.findByBookAndUser(bookId, userId)
//                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found for bookId: " + bookId));
//
//        // You can add additional checks if needed, e.g., checking reservation status, etc.
//
//        // Delete the reservation from the database
//        reservationRepository.delete(reservation);
//    }

//    public void cancelReservation(Long bookId, Long userId) {
//        // Find the reservation using Optional
//        Optional<Book> optionalBook = bookRepository.findByBookId(bookId);
//        Book book;
//        if (optionalBook.isPresent()) {
//            book = optionalBook.get();
//        } else {
//            // Reservation not found, throw an exception
//            throw new ReservationNotFoundException("Reservation not found for bookId: " + bookId);
//        }
//
//        Optional<User> optionalUser = userRepository.findByUserId(userId);
//        User user;
//        if (optionalUser.isPresent()) {
//            user = optionalUser.get();
//        } else {
//            // Reservation not found, throw an exception
//            throw new ReservationNotFoundException("Reservation not found for userId: " + userId);
//        }
//        Optional<BookReservation> optionalReservation = reservationRepository.findByUserAndBook(user, book);
//
//        // Check if the reservation exists
//        if (optionalReservation.isPresent()) {
//            // Reservation found, proceed to delete it
//            reservationRepository.delete(optionalReservation.get());
//        } else {
//            // Reservation not found, throw an exception
//            throw new ReservationNotFoundException("Reservation not found for bookId: " + bookId);
//        }
//    }

//    public BookReservation findByReservationId(Long reservationId) {
//        Optional<BookReservation> reservation = reservationRepository.findById(reservationId);
//
//        if (reservation.isEmpty()) {
//            throw new ReservationNotFoundException("Reservation with ID " + reservationId + " not found.");
//        }
//
//        return reservation.get();
//    }
}