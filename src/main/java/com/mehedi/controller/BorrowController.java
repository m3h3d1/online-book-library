package com.mehedi.controller;

import com.mehedi.dto.BorrowRequest;
import com.mehedi.exception.BookNotFoundException;
import com.mehedi.exception.BookReturnException;
import com.mehedi.exception.UserNotFoundException;
import com.mehedi.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;

    @PostMapping("/{bookId}/borrow")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId, @RequestBody BorrowRequest borrowRequest) {
        try {
            borrowService.borrowBook(bookId, borrowRequest.getDueDate());
            return new ResponseEntity<>("Book borrowed successfully", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to borrow the book: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{bookId}/return")
    public ResponseEntity<String> returnBook(@PathVariable Long bookId) {
        try {
            borrowService.returnBook(bookId);
            return new ResponseEntity<>("Book returned successfully", HttpStatus.OK);
        } catch (BookReturnException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (BookNotFoundException | UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
