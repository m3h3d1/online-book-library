package com.mehedi.repository;

import com.mehedi.entity.Book;
import com.mehedi.entity.BookReservation;
import com.mehedi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
//    List<BookReservation> findByBookId(Long bookId);
//    List<BookReservation> findByUserId(Long userId);
//    List<BookReservation> findByBookIdAndUserId(Long bookId, Long userId);
    List<BookReservation> findByUser(User user);
    List<BookReservation> findByBook(Book book);
    List<BookReservation> findByUserAndBook(User user, Book book);
//    Optional<BookReservation> findByReservationId(Long reservationId);
//    Optional<BookReservation> findByBookIdAndUserId(Long bookId, Long userId);
}