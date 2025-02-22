package com.mehedi.controller;

import com.mehedi.dto.BookReviewDTO;
import com.mehedi.dto.BookReviewRequest;
import com.mehedi.dto.ReviewRequest;
import com.mehedi.entity.BookReview;
import com.mehedi.exception.ReviewNotFoundException;
import com.mehedi.exception.UnauthorizedUserException;
import com.mehedi.service.BookReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books/{bookId}/reviews")
public class BookReviewController {

    private final BookReviewService bookReviewService;

    @Autowired
    public BookReviewController(BookReviewService bookReviewService) {
        this.bookReviewService = bookReviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createReview(
            @PathVariable Long bookId,
            @RequestBody BookReviewRequest reviewRequest
    ) {
        Long userId = 1L; //*******
        bookReviewService.createReview(
                bookId,
                userId,
                reviewRequest.getRating(),
                reviewRequest.getComment()
        );
        return new ResponseEntity<>("Review created successfully", HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<BookReviewDTO>> getReviewsForBook(@PathVariable Long bookId) {
        List<BookReviewDTO> reviews = bookReviewService.getReviewsByBookId(bookId);
        if (reviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PutMapping("/{reviewId}/update")
    public ResponseEntity<String> updateReviewAndRating(
            @PathVariable Long bookId,
            @PathVariable Long reviewId,
            @RequestBody ReviewRequest reviewRequest
    ) {
        Long userId = 1L;
        Integer newRating = reviewRequest.getRating();
        String newComment = reviewRequest.getComment();
        try {
            bookReviewService.updateReviewAndRating(userId, bookId, reviewId, newRating, newComment);
            return new ResponseEntity<>("Review updated successfully", HttpStatus.OK);
        } catch (ReviewNotFoundException e) {
            return new ResponseEntity<>("Review not found for the given book and review IDs", HttpStatus.NOT_FOUND);
        } catch (UnauthorizedUserException e) {
            return new ResponseEntity<>("You are not authorized to update this review", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{reviewId}/delete")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long bookId,
            @PathVariable Long reviewId
    ) {
        Long userId = 1L;
        try {
            bookReviewService.deleteReview(bookId, reviewId, userId);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (ReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
