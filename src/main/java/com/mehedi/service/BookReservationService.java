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

        if (book.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
            throw new ErrorMessage("The book is already available.");
        }

        // Check if the user has already reserved the same book
        List<BookReservation> existingReservations = reservationRepository.findByUserAndBook(user, book);
        if (!existingReservations.isEmpty()) {
            throw new ErrorMessage("An user already reserved this book.");
        }

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

    public void cancelReservation(Long bookId, Long userId) {
//        BookReservation reservation = reservationRepository.findById(reservationId)
        Optional<Book> bookOptional = bookRepository.findByBookId(bookId);
        Optional<User> userOptional = userRepository.findById(userId);
        if(!bookOptional.isPresent()) {
            throw new BookNotFoundException("This book is not found.");
        }
        Book book = bookOptional.get();
        User user = userOptional.get();
        BookReservation reservation = reservationRepository.findByBookAndUser(book, user)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with this book: " + bookId));

        if (!reservation.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedUserException("You are not authorized to cancel this reservation.");
        }

        if (reservation.getReservationStatus() == ReservationStatus.CANCELED) {
            throw new BookReservationException("The reservation is already canceled.");
        }

        reservation.setReservationStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
    }
}
