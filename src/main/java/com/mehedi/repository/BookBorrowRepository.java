package com.mehedi.repository;

import com.mehedi.entity.Book;
import com.mehedi.entity.BookBorrow;
import com.mehedi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookBorrowRepository extends JpaRepository<BookBorrow, Long> {
    BookBorrow findByBookAndUser(Book book, User user);
    List<BookBorrow> findByUser(User user);
}
