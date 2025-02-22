package com.mehedi.service;

import com.mehedi.entity.Book;
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

//    public Optional<List<BookReview>> getReviewsByBookId(Long bookId) {
////        return Optional<List<BookReview>> reviews = bookReviewRepository.findByBookId(bookId);
//        return reviewRepository.findByBookId(bookId);
//    }
//    public List<BookReview> getReviewsAndRatingsByBookId(Long bookId) {
//        return reviewRepository.findByBook_Id(bookId);
////                .orElseThrow(() -> new BookNotFoundException("No reviews found for book with id: " + bookId));
//    }

    public void updateReviewAndRating(Long reviewId, Integer newRating, String newComment) {
        BookReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));

        // Check if the user is allowed to update their own review (CUSTOMER check here)
        // You may add additional logic to validate user permissions.

        // Update the review's rating and comment
        review.setRating(newRating);
        review.setComment(newComment);

        // Save the updated review
        reviewRepository.save(review);
    }

    public void deleteReview(Long bookId, Long reviewId, Long userId) {
        // Check if the review exists
        BookReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));

        // Check if the user is the author of the review
        if (!review.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedUserException("You are not authorized to delete this review");
        }

        // Delete the review
        reviewRepository.delete(review);
    }
}
