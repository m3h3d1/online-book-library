package com.mehedi.service;

import com.mehedi.constatnts.AvailabilityStatus;
import com.mehedi.dto.BookBorrowDTO;
import com.mehedi.dto.BorrowHistoryDTO;
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
import java.util.*;

@Service
public class BorrowService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookBorrowRepository bookBorrowRepository;

    @Transactional
    public void borrowBook(Long bookId, LocalDate dueDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();

        if (!userNow.getRole().equals(User.Role.CUSTOMER)) {
            throw new UnauthorizedUserException("You are not authorized to access this!");
        }

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

        if (!userNow.getRole().equals(User.Role.CUSTOMER)) {
            throw new UnauthorizedUserException("You are not authorized to access this!");
        }

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

    public List<BorrowHistoryDTO> getUserBorrowHistory(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();
//        Long userId = userNow.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        List<BookBorrow> borrowHistory = bookBorrowRepository.findByUser(user);

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


    public List<BookBorrowDTO> getAllBookByUser(Long userId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();

        if (!userNow.getUserId().equals(userId) && userNow.getRole().equals("CUSTOMER")) {
            throw new UnauthorizedUserException("You are not authorized to access this!");
        }
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException("User Not Found"));

        List<BookBorrow> booksBorrowed = bookBorrowRepository.findByUser(user);


        List<BookBorrowDTO> booksDTO = new ArrayList<>();
        for (BookBorrow bookBorrow : booksBorrowed) {
            BookBorrowDTO bookDTO = new BookBorrowDTO();
            bookDTO.setBookId(bookBorrow.getBook().getBookId());
            bookDTO.setTitle(bookBorrow.getBook().getTitle());
            bookDTO.setAuthor(bookBorrow.getBook().getAuthor());
            bookDTO.setAvailabilityStatus(bookBorrow.getBook().getAvailabilityStatus());
            booksDTO.add(bookDTO);
        }
        return booksDTO;
    }

    public Set<?> currentlyBorrowedBooks(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();

        if (!userNow.getUserId().equals(userId) && userNow.getRole().equals("CUSTOMER")) {
            throw new UnauthorizedUserException("You are not authorized to access this!");
        }

        Optional<User> optionalUser=userRepository.findByUserId(userId);

        if(!optionalUser.isPresent()) {
            throw new UserNotFoundException("User Not Found");
        }
        User user=optionalUser.get();

        List<Book> borrowedList = bookRepository.findByUserAndAvailabilityStatus(user,AvailabilityStatus.BORROWED);

        if(userNow.getRole().equals(User.Role.ADMIN)) {
            Set<BookBorrowDTO> borrowedBooks = new HashSet<>();
            for(Book book: borrowedList) {
                BookBorrowDTO bookDto = new BookBorrowDTO();
                bookDto.setBookId(book.getBookId());
                bookDto.setTitle(book.getTitle());
                bookDto.setAuthor(book.getAuthor());
                bookDto.setAvailabilityStatus(book.getAvailabilityStatus());
                borrowedBooks.add(bookDto);
            }
            return borrowedBooks;
        }

        if(userNow.getRole().equals(User.Role.CUSTOMER) && userNow.getUserId().equals(userId)) {
            Set<BookBorrowDTO> borrowedBooks = new HashSet<>();
            for(Book book: borrowedList) {
                BookBorrowDTO bookDto = new BookBorrowDTO();
                bookDto.setBookId(book.getBookId());
                bookDto.setTitle(book.getTitle());
                bookDto.setAuthor(book.getAuthor());
                bookDto.setAvailabilityStatus(book.getAvailabilityStatus());
                borrowedBooks.add(bookDto);
            }
            return borrowedBooks;
        }
        return null;
    }
}
