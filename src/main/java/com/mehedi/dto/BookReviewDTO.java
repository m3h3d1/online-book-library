package com.mehedi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewDTO {
    private Long reviewId;
    private Integer rating;
    private String comment;
    private Long userId;
    private String username;
}