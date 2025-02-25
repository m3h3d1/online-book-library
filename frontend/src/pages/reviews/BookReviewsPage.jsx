import React, { useState, useEffect } from 'react';
import axiosInstance from '../../utils/axiosInstance';
import { useParams } from 'react-router-dom';
import AddReviewForm from './AddReviewForm';
import EditReviewForm from './EditReviewForm';
import {
  Button,
  Grid,
  List,
  ListItem,
  ListItemText,
  Typography,
} from '@mui/material';
import AverageRating from '../books/AverageRating';

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

  const calculateAverageRating = (items) => {
    if (items.length === 0) {
      return 0;
    }
  
    const totalRating = items.reduce((total, item) => total + item.rating, 0);
    return totalRating / items.length;
  };
  
  const averageRating = calculateAverageRating(reviews);

  return (
    <div>
      <Typography variant="h4" gutterBottom>
        <center>Reviews and Ratings</center>
      </Typography>
      <AverageRating averageRating={averageRating} />
      <Button onClick={() => handleEditReview('add')} variant="contained" color="primary">
        Add Review
      </Button>
      {loading ? (
        <Typography>Loading reviews...</Typography>
      ) : (
        <List>
          {reviews.map((review) => (
            <ListItem key={review.reviewId}>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={4}>
                  <ListItemText primary={`Rating: ${review.rating}`} secondary={`Comment: ${review.comment}`} />
                </Grid>
                <Grid item xs={12} sm={4}>
                  <ListItemText primary={`Posted by: ${review.username}`} />
                </Grid>
                <Grid item xs={12} sm={4}>
                  {editingReview === review.reviewId && (
                    <EditReviewForm
                      bookId={bookId}
                      reviewId={review.reviewId}
                      initialRating={review.rating}
                      initialComment={review.comment}
                      onUpdate={handleReviewUpdated}
                    />
                  )}
                  <Button onClick={() => handleEditReview(review.reviewId)} variant="contained" color="primary">
                    Update Review
                  </Button>
                  <Button onClick={() => handleDeleteReview(review.reviewId)} variant="contained" color="error">
                    Delete Review
                  </Button>
                </Grid>
              </Grid>
            </ListItem>
          ))}
          {editingReview === 'add' && (
            <AddReviewForm bookId={bookId} onReviewAdded={handleReviewAdded} />
          )}
        </List>
      )}
    </div>
  );
};

export default BookReviewsPage;
