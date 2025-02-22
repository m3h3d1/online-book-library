package com.mehedi.controller;

import com.mehedi.exception.BookReservationException;
import com.mehedi.exception.ReservationNotFoundException;
import com.mehedi.exception.UnauthorizedUserException;
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

    @DeleteMapping("/cancel-reservation")
    public ResponseEntity<?> cancelReservation(@PathVariable Long bookId) {
        Long userId = 2L;
        try {
            reservationService.cancelReservation(bookId, userId);
            return ResponseEntity.ok("Reservation canceled successfully.");
        } catch (ReservationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found.");
        } catch (UnauthorizedUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to cancel this reservation.");
        } catch (BookReservationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel reservation: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel reservation.");
        }
    }
}
