import React, { useState } from 'react';
import axiosInstance from '../../utils/axiosInstance';

const AddReviewForm = ({ bookId, onReviewAdded }) => {
  const [rating, setRating] = useState(1); // Default rating is set to 1
  const [comment, setComment] = useState('');
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

    const newReview = {
      rating,
      comment,
    };

    axiosInstance
      .post(`/books/${bookId}/reviews/create`, newReview)
      .then((response) => {
        onReviewAdded(response.data);
        setIsSubmitting(false);
        setRating(1);
        setComment('');
      })
      .catch((error) => {
        console.error('Error adding review:', error);
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
        Submit Review
      </button>
    </form>
  );
};

export default AddReviewForm;