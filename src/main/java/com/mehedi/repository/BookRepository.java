package com.mehedi.repository;

import com.mehedi.constatnts.AvailabilityStatus;
import com.mehedi.entity.Book;
import com.mehedi.entity.BookBorrow;
import com.mehedi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleAndAuthor(String title, String author);
    Optional<Book> findByBookId(Long bookId);
    List<Book> findByUserAndAvailabilityStatus(User user, AvailabilityStatus availabilityStatus);

    List<Book> findByAvailabilityStatus(AvailabilityStatus availabilityStatus);
}
