package com.mehedi.repository;

import com.mehedi.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
//    List<BookReview> findByBook_Id(Long bookId);
}
