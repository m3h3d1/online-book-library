package com.mehedi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BorrowRequest {
    private Long userId;
    private LocalDate dueDate;
}
