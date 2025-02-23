import React, { useState, useEffect } from 'react';
import axiosInstance from '../../utils/axiosInstance';
import { useParams } from 'react-router-dom';
import AddReviewForm from './AddReviewForm';
import EditReviewForm from './EditReviewForm';

const BookReviewsPage = () => {
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingReview, setEditingReview] = useState(null);

  const { bookId } = useParams();

  useEffect(() => {
    axiosInstance.get(`/books/${bookId}/reviews`)
      .then((response) => {
        setReviews(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching reviews:', error);
        setLoading(false);
      });
  }, [bookId]);

  const handleEditReview = (reviewId) => {
    setEditingReview(reviewId);
  };

  const handleCancelEdit = () => {
    setEditingReview(null);
  };

  const handleReviewAdded = (newReview) => {
    setReviews([...reviews, newReview]);
  };

  const handleReviewUpdated = (updatedReview) => {
    setReviews(reviews.map((review) =>
      review.reviewId === updatedReview.reviewId ? updatedReview : review
    ));
    setEditingReview(null);
  };

  const handleDeleteReview = (reviewId) => {
    axiosInstance
      .delete(`/books/${bookId}/reviews/${reviewId}/delete`)
      .then(() => {
        setReviews(reviews.filter((review) => review.reviewId !== reviewId));
      })
      .catch((error) => {
        console.error('Error deleting review:', error);
      });
  };  

  return (
    <div>
      <h2>Reviews and Ratings</h2>
      <button onClick={() => handleEditReview('add')}>Add Review</button>
      {loading ? (
        <p>Loading reviews...</p>
      ) : (
        <ul>
          {reviews.map((review) => (
            <li key={review.reviewId}>
              <p>Rating: {review.rating}</p>
              <p>Comment: {review.comment}</p>
              <p>Posted by: {review.username}</p>
              {editingReview === review.reviewId && (
                <EditReviewForm
                  bookId={bookId}
                  reviewId={review.reviewId}
                  initialRating={review.rating}
                  initialComment={review.comment}
                  onUpdate={handleReviewUpdated}
                />
              )}
              <button onClick={() => handleEditReview(review.reviewId)}>
                Update Review
              </button>
              <button onClick={() => handleDeleteReview(review.reviewId)}>
                    Delete Review
                </button>
            </li>
          ))}
          {editingReview === 'add' && (
            <AddReviewForm bookId={bookId} onReviewAdded={handleReviewAdded} />
          )}
        </ul>
      )}
    </div>
  );
};

export default BookReviewsPage;