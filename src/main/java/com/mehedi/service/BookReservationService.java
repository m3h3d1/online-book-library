package com.mehedi.service;

import com.mehedi.constatnts.AvailabilityStatus;
import com.mehedi.constatnts.ReservationStatus;
import com.mehedi.dto.BookReservationDTO;
import com.mehedi.entity.Book;
import com.mehedi.entity.BookReservation;
import com.mehedi.entity.User;
import com.mehedi.exception.*;
import com.mehedi.repository.BookRepository;
import com.mehedi.repository.BookReservationRepository;
import com.mehedi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

    public void reserveBook(Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();

        if(!userNow.getRole().equals(User.Role.CUSTOMER)) {
            throw new UnauthorizedUserException("You are not Authorized");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        if (book.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
            throw new ErrorMessage("The book is already available.");
        }

        List<BookReservation> existingReservations = reservationRepository.findByBookAndReservationStatus(book, ReservationStatus.RESERVED);
        if (!existingReservations.isEmpty()) {
            throw new ErrorMessage("An user already reserved this book.");
        }

        BookReservation reservation = new BookReservation();
        reservation.setBook(book);
        reservation.setUser(userNow);
        reservation.setReservationDate(LocalDate.now());
        reservation.setNotified(false);
        reservation.setReservationStatus(ReservationStatus.RESERVED);

        reservationRepository.save(reservation);
    }

    public void cancelReservation(Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();

        if(!userNow.getRole().equals(User.Role.CUSTOMER)) {
            throw new UnauthorizedUserException("You are not Authorized");
        }

        Optional<Book> bookOptional = bookRepository.findByBookId(bookId);
        if(!bookOptional.isPresent()) {
            throw new BookNotFoundException("This book is not found.");
        }
        Book book = bookOptional.get();

        BookReservation reservation = reservationRepository.findByBookAndUser(book, userNow)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with this book: " + bookId));

        reservationRepository.delete(reservation);
    }

    public Set<BookReservationDTO> currentlyReservedBooks(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();

        if (!userNow.getUserId().equals(userId) && userNow.getRole().equals(User.Role.CUSTOMER)) {
            throw new UnauthorizedUserException("You are not authorized to access this!");
        }

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("User Not Found");
        }
        User user = optionalUser.get();

        List<BookReservation> reservedList = reservationRepository.findByUserAndReservationStatus(user, ReservationStatus.RESERVED);

        if (userNow.getRole().equals(User.Role.ADMIN)) {
            Set<BookReservationDTO> reservedBooks = new HashSet<>();
            for (BookReservation reservation : reservedList) {
                Book book = reservation.getBook();
                BookReservationDTO reservationDto = new BookReservationDTO();
                reservationDto.setBookId(book.getBookId());
                reservationDto.setTitle(book.getTitle());
                reservationDto.setAuthor(book.getAuthor());
                reservationDto.setReservationDate(reservation.getReservationDate());
                reservationDto.setNotified(reservation.isNotified());
                reservationDto.setReservationStatus(reservation.getReservationStatus().toString());
                reservedBooks.add(reservationDto);
            }
            return reservedBooks;
        }

        if (userNow.getRole().equals(User.Role.CUSTOMER) && userNow.getUserId().equals(userId)) {
            Set<BookReservationDTO> reservedBooks = new HashSet<>();
            for (BookReservation reservation : reservedList) {
                Book book = reservation.getBook();
                BookReservationDTO reservationDto = new BookReservationDTO();
                reservationDto.setBookId(book.getBookId());
                reservationDto.setTitle(book.getTitle());
                reservationDto.setAuthor(book.getAuthor());
                reservationDto.setReservationDate(reservation.getReservationDate());
                reservationDto.setNotified(reservation.isNotified());
                reservationDto.setReservationStatus(reservation.getReservationStatus().toString());
                reservedBooks.add(reservationDto);
            }
            return reservedBooks;
        }
        return Collections.emptySet();
    }
}
