import React, { useState, useEffect } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import axiosInstance from '../../utils/axiosInstance';
import {
  CircularProgress,
  Typography,
  Button,
  Card,
  CardContent,
  CardMedia,
  Grid,
  TextField,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import BookBorrow from './borrow/BookBorrow';
import BookReserve from './reserve/BookReserve';

const BookDetailsPage = () => {
  const { bookId } = useParams();
  const navigate = useNavigate();
  const [bookData, setBookData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editMode, setEditMode] = useState(false);
  const [updatedBookData, setUpdatedBookData] = useState({});
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const role = localStorage.getItem('role');

  useEffect(() => {
    axiosInstance.get(`/books/${bookId}`)
      .then((response) => {
        setBookData(response.data);
        setUpdatedBookData(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching book data:', error);
        setLoading(false);
      });
  }, [bookId]);

  const handleEditClick = () => {
    setEditMode(!editMode);
  };

  const handleUpdateClick = () => {
    axiosInstance.put(`/books/update/${bookId}`, updatedBookData)
      .then((response) => {
        console.log('Book updated:', response.data);
        setBookData(updatedBookData);
        setEditMode(false);
      })
      .catch((error) => {
        console.error('Error updating book:', error);
      });
  };

  const handleDeleteClick = () => {
    setDeleteConfirmationOpen(true);
  };

  const handleDeleteConfirmation = () => {
    axiosInstance.delete(`/books/delete/${bookId}`)
      .then(() => {
        console.log('Book deleted successfully');
        navigate('/');
      })
      .catch((error) => {
        console.error('Error deleting book:', error);
      });
  };

  const handleBorrowSuccess = (borrowedBook) => {
    setBookData(borrowedBook);
  };

  const handleReserveSuccess = (reservedBook) => {
    setBookData(reservedBook);
  };

  return (
    <div>
      {loading ? (
        <CircularProgress />
      ) : (
        <div>
          {bookData ? (
            <div>
              <Typography variant="h4" gutterBottom>
                Book Information
              </Typography>
              <Grid container spacing={3}>
                <Grid item xs={12} sm={6}>
                  <Card>
                    <CardMedia
                      component="img"
                      alt={bookData.title}
                      style={{ height: '100%', width: '100%', objectFit: 'cover' }}
                      image={bookData.coverUrl}
                    />
                  </Card>
                </Grid>
                <Grid item xs={12} sm={6}>
                  <Card>
                    <CardContent>
                      {editMode ? (
                        <div>
                          <TextField
                            label="Title"
                            fullWidth
                            value={updatedBookData.title}
                            onChange={(e) => setUpdatedBookData({ ...updatedBookData, title: e.target.value })}
                          />
                          <TextField
                            label="Author"
                            fullWidth
                            value={updatedBookData.author}
                            onChange={(e) => setUpdatedBookData({ ...updatedBookData, author: e.target.value })}
                          />
                        </div>
                      ) : (
                        <div>
                          <Typography variant="h6" component="div">
                            {bookData.title}
                          </Typography>
                          <Typography variant="body2" color="textSecondary">
                            Author: {bookData.author}
                          </Typography>
                          <Typography variant="body2" color="textSecondary">
                            Availability Status: {bookData.availabilityStatus}
                          </Typography>
                        </div>
                      )}
                      {role === 'ADMIN' && !editMode ? (
                        <div>
                          <Button variant="contained" color="primary" onClick={handleEditClick}>
                            Update Book
                          </Button>
                          <Button
                            variant="contained"
                            color="error"
                            onClick={handleDeleteClick}
                          >
                            Delete Book
                          </Button>
                        </div>
                      ) : null}
                      {role === 'CUSTOMER' && !editMode && bookData.availabilityStatus === 'BORROWED' ? (
                        <BookReserve bookId={bookId} onReserveSuccess={handleReserveSuccess} />
                      ) : null}
                      {role === 'CUSTOMER' && !editMode && bookData.availabilityStatus === 'AVAILABLE' ? (
                        <BookBorrow bookId={bookId} onBorrowSuccess={handleBorrowSuccess} />
                      ) : null}
                      {editMode ? (
                        <Button variant="contained" color="primary" onClick={handleUpdateClick}>
                          Confirm Update
                        </Button>
                      ) : null}
                    </CardContent>
                  </Card>
                </Grid>
              </Grid>
              <Link to="/">Back to All Books</Link>
            </div>
          ) : (
            <Typography variant="h6">Book not found.</Typography>
          )}
        </div>
      )}
      <Dialog
        open={deleteConfirmationOpen}
        onClose={() => setDeleteConfirmationOpen(false)}
      >
        <DialogTitle>Confirm Deletion</DialogTitle>
        <DialogContent>
          Are you sure you want to delete this book?
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteConfirmationOpen(false)} color="primary">
            Cancel
          </Button>
          <Button onClick={handleDeleteConfirmation} color="error">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default BookDetailsPage;
