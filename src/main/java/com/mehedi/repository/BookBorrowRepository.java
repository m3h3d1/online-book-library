package com.mehedi.repository;

import com.mehedi.entity.Book;
import com.mehedi.entity.BookBorrow;
import com.mehedi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookBorrowRepository extends JpaRepository<BookBorrow, Long> {
    // You can add custom query methods here if needed
    BookBorrow findByBookAndUser(Book book, User user);
}
