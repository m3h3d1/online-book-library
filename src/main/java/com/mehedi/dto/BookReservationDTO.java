package com.mehedi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookReservationDTO {
    private Long bookId;
    private String title;
    private String author;
    private LocalDate reservationDate;
    private boolean isNotified;
    private String reservationStatus;
}
