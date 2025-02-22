package com.mehedi.controller;

import com.mehedi.service.BookReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books/{bookId}")
public class BookReservationController {
    private final BookReservationService reservationService;

    @Autowired
    public BookReservationController(BookReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveBook(@PathVariable Long bookId) {
        Long userId = 2L;
        reservationService.reserveBook(bookId, userId);
        return new ResponseEntity<>("Book reserved successfully.", HttpStatus.OK);
    }
}
