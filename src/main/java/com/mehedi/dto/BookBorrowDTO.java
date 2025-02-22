package com.mehedi.dto;

import com.mehedi.constatnts.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookBorrowDTO {
    private Long bookId;
    private String title;
    private String author;
    private AvailabilityStatus availabilityStatus;
}