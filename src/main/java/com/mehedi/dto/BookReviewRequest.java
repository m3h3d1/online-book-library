package com.mehedi.dto;

public class BookReviewRequest {

    private Long userId;
    private Integer rating;
    private String comment;

    // Constructors, getters, and setters

    public BookReviewRequest() {
    }

    public BookReviewRequest(Long userId, Integer rating, String comment) {
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
