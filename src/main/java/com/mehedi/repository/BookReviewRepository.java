package com.mehedi.repository;

import com.mehedi.entity.Book;
import com.mehedi.entity.BookReview;
import com.mehedi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    List<BookReview> findByBook(Book book);
    Optional<BookReview> findByReviewIdAndUserAndBook(Long reviewId, User user, Book book);
}
