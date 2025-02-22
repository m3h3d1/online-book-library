package com.mehedi.service;

import com.mehedi.constatnts.AvailabilityStatus;
import com.mehedi.dto.BorrowHistoryDTO;
import com.mehedi.dto.BorrowRequest;
import com.mehedi.entity.Book;
import com.mehedi.entity.BookBorrow;
import com.mehedi.entity.User;
import com.mehedi.exception.*;
import com.mehedi.repository.BookBorrowRepository;
import com.mehedi.repository.BookRepository;
import com.mehedi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BorrowService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookBorrowRepository bookBorrowRepository;

    @Transactional
    public void borrowBook(Long bookId, Long temp, LocalDate dueDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();
        Long userId = userNow.getUserId();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        if (book.getAvailabilityStatus() == AvailabilityStatus.BORROWED) {
            throw new BookUnavailableException("Book is already borrowed.");
        }

        book.setAvailabilityStatus(AvailabilityStatus.BORROWED);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        BookBorrow bookBorrow = new BookBorrow();
        bookBorrow.setUser(user);
        bookBorrow.setBook(book);
        bookBorrow.setBorrowDate(LocalDate.now());
        bookBorrow.setDueDate(dueDate);
        bookBorrow.setReturnDate(null);

        bookRepository.save(book);
        bookBorrowRepository.save(bookBorrow);
    }



    public void returnBook(Long bookId, Long temp) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();
        Long userId = userNow.getUserId();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        if (!book.getAvailabilityStatus().equals(AvailabilityStatus.BORROWED)) {
            throw new BookReturnException("Book is not borrowed by the user.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        BookBorrow bookBorrow = bookBorrowRepository.findByBookAndUser(book, user);

        if (bookBorrow == null) {
            throw new BookReturnException("No borrow record found for this user and book.");
        }

        book.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        book.setUser(null);

        bookBorrow.setReturnDate(LocalDate.now());

        bookRepository.save(book);
        bookBorrowRepository.save(bookBorrow);
    }

    @Service
    public class BorrowBookService {

        @Autowired
        private BookBorrowRepository bookBorrowRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private BookRepository bookRepository;

        public List<BorrowHistoryDTO> getUserBorrowHistory(Long userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

            List<BookBorrow> borrowHistory = bookBorrowRepository.findByUser(user);

            // Convert the BookBorrow entities to BorrowHistoryDTO objects
            List<BorrowHistoryDTO> historyDTOList = new ArrayList<>();
            for (BookBorrow borrow : borrowHistory) {
                BorrowHistoryDTO historyDTO = new BorrowHistoryDTO();
                historyDTO.setBookTitle(borrow.getBook().getTitle());
                historyDTO.setDueDate(borrow.getDueDate());
                historyDTO.setReturnDate(borrow.getReturnDate());
                historyDTOList.add(historyDTO);
            }

            return historyDTOList;
        }
    }

    public List<BorrowHistoryDTO> getUserBorrowHistory(Long temp) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();
        Long userId = userNow.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Retrieve the borrowing history for the user
        List<BookBorrow> borrowHistory = bookBorrowRepository.findByUser(user);

        // Convert the BookBorrow entities to BorrowHistoryDTO objects
        List<BorrowHistoryDTO> historyDTOList = new ArrayList<>();
        for (BookBorrow borrow : borrowHistory) {
            BorrowHistoryDTO historyDTO = new BorrowHistoryDTO();
            historyDTO.setBookTitle(borrow.getBook().getTitle());
            historyDTO.setDueDate(borrow.getDueDate());
            historyDTO.setReturnDate(borrow.getReturnDate());
            historyDTOList.add(historyDTO);
        }

        return historyDTOList;
    }
}
