import React from 'react';
import {
  Typography,
  Button,
  TextField,
} from '@mui/material';
import BookBorrow from './borrow/BookBorrow';
import BookReturn from './borrow/BookReturn';

const BookInformation = ({
  bookData,
  editMode,
  updatedBookData,
  handleEditClick,
  handleUpdateClick,
  handleDeleteClick,
  handleBorrowSuccess,
  handleReturnSuccess,
}) => {
  return (
    <div>
      {editMode ? (
        <div>
          <TextField
            label="Title"
            fullWidth
            value={updatedBookData.title}
            onChange={(e) => handleUpdateClick({ ...updatedBookData, title: e.target.value })}
          />
          <TextField
            label="Author"
            fullWidth
            value={updatedBookData.author}
            onChange={(e) => handleUpdateClick({ ...updatedBookData, author: e.target.value })}
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
      {editMode ? (
        <Button variant="contained" color="primary" onClick={handleUpdateClick}>
          Confirm Update
        </Button>
      ) : (
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
          <BookBorrow bookId={bookData.id} onBorrowSuccess={handleBorrowSuccess} />
          <BookReturn bookId={bookData.id} onReturnSuccess={handleReturnSuccess} />
        </div>
      )}
    </div>
  );
};

export default BookInformation;