import React, { useState, useEffect } from 'react';
import axiosInstance from '../../utils/axiosInstance';

const EditReviewForm = ({ bookId, reviewId, initialRating, initialComment, onUpdate }) => {
  const [rating, setRating] = useState(initialRating);
  const [comment, setComment] = useState(initialComment);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleRatingChange = (event) => {
    const newRating = Math.min(Math.max(1, parseInt(event.target.value, 10)), 5);
    setRating(newRating);
  };

  const handleCommentChange = (event) => {
    setComment(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    setIsSubmitting(true);

    const updatedReview = {
      rating,
      comment,
    };

    axiosInstance
      .put(`/books/${bookId}/reviews/${reviewId}/update`, updatedReview)
      .then((response) => {
        onUpdate(response.data);
        setIsSubmitting(false);
      })
      .catch((error) => {
        console.error('Error updating review:', error);
        setIsSubmitting(false);
      });
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>
        Rating (1 to 5):
        <input
          type="number"
          min="1"
          max="5"
          value={rating}
          onChange={handleRatingChange}
        />
      </label>
      <br />
      <label>
        Comment:
        <textarea value={comment} onChange={handleCommentChange} />
      </label>
      <br />
      <button type="submit" disabled={isSubmitting}>
        Submit Updated Review
      </button>
    </form>
  );
};

export default EditReviewForm;