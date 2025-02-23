package com.mehedi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BorrowHistoryDTO {
    private Long bookId;
    private String bookTitle;
    private LocalDate dueDate;
    private LocalDate returnDate;
}