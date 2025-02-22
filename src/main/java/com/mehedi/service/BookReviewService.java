package com.mehedi.service;

import com.mehedi.dto.BookReviewDTO;
import com.mehedi.dto.BorrowHistoryDTO;
import com.mehedi.entity.Book;
import com.mehedi.entity.BookBorrow;
import com.mehedi.entity.BookReview;
import com.mehedi.entity.User;
import com.mehedi.exception.BookNotFoundException;
import com.mehedi.exception.ReviewNotFoundException;
import com.mehedi.exception.UnauthorizedUserException;
import com.mehedi.exception.UserNotFoundException;
import com.mehedi.repository.BookRepository;
import com.mehedi.repository.BookReviewRepository;
import com.mehedi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookReviewService {

    private final BookReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookReviewService(BookReviewRepository reviewRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public void createReview(Long bookId, Long userId, Integer rating, String comment) {
        Optional<Book> optionalBook = bookRepository.findByBookId(bookId);
        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if (optionalBook.isPresent() && optionalUser.isPresent()) {
            Book book = optionalBook.get();
            User user = optionalUser.get();

            BookReview review = new BookReview();
            review.setBook(book);
            review.setUser(user);
            review.setRating(rating);
            review.setComment(comment);

            reviewRepository.save(review);
        } else {
            if (!optionalBook.isPresent()) {
                throw new BookNotFoundException("Book not found with id: " + bookId);
            }
            if (!optionalUser.isPresent()) {
                throw new UserNotFoundException("User not found with id: " + userId);
            }
        }
    }

    public void updateReviewAndRating(Long userId, Long bookId, Long reviewId, Integer newRating, String newComment) {
        Optional<Book> optionalBook = bookRepository.findByBookId(bookId);
        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if(!optionalBook.isPresent() || !optionalUser.isPresent()) {
            throw new ReviewNotFoundException("You didn't review this book");
        }

        Book book = optionalBook.get();
        User user = optionalUser.get();

        Optional<BookReview> reviewOptional = reviewRepository.findByReviewIdAndUserAndBook(reviewId, user, book);
        if(!reviewOptional.isPresent()) {
            throw new ReviewNotFoundException("You didn't review this book");
        }

        BookReview review = reviewOptional.get();

        if (!review.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedUserException("You are not authorized to update this review");
        }

        review.setRating(newRating);
        review.setComment(newComment);

        reviewRepository.save(review);
    }

    public void deleteReview(Long bookId, Long reviewId, Long userId) {
        BookReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));

        if (!review.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedUserException("You are not authorized to delete this review");
        }

        reviewRepository.delete(review);
    }

    public List<BookReviewDTO> getReviewsByBookId(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findByBookId(bookId);
        if(!bookOptional.isPresent()) {
            throw new BookNotFoundException("This book is not found");
        }
        Book book = bookOptional.get();

        List<BookReview> bookReviews = reviewRepository.findByBook(book);

        List<BookReviewDTO> bookReviewsDTO = new ArrayList<>();
        for (BookReview review : bookReviews) {
            BookReviewDTO bookReviewDTO = new BookReviewDTO();
            bookReviewDTO.setReviewId(review.getReviewId());
            bookReviewDTO.setRating(review.getRating());
            bookReviewDTO.setComment(review.getComment());
            bookReviewDTO.setUserId(review.getUser().getUserId());
            bookReviewDTO.setUsername(review.getUser().getFirstName()+" "+review.getUser().getLastName());
            bookReviewsDTO.add(bookReviewDTO);
        }
        return bookReviewsDTO;
    }
}
