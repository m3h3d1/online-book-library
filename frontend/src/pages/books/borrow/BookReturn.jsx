import React, { useState } from 'react';
import axiosInstance from '../../../utils/axiosInstance';
import { Button, Dialog, DialogTitle, DialogContent, DialogActions, Typography } from '@mui/material';

const BookReturn = ({ bookId, onReturnSuccess }) => {
  const [returnDialogOpen, setReturnDialogOpen] = useState(false);
  const [returnMessage, setReturnMessage] = useState('');

  const handleReturnClick = () => {
    setReturnDialogOpen(true);
  };

  const handleReturnConfirmation = () => {
    axiosInstance
      .put(`/books/${bookId}/return`)
      .then((response) => {
        console.log('Book returned successfully');
        setReturnMessage('Book returned successfully');
        onReturnSuccess(response.data);
        setReturnDialogOpen(false);
      })
      .catch((error) => {
        console.error('Error returning book:', error);
        setReturnMessage('Error returning book. Please try again.');
        setReturnDialogOpen(false);
      });
  };

  return (
    <div>
      <Button variant="contained" color="primary" onClick={handleReturnClick}>
        Return Book
      </Button>
      <Dialog open={returnDialogOpen} onClose={() => setReturnDialogOpen(false)}>
        <DialogTitle>Return Book</DialogTitle>
        <DialogContent>
          Are you sure you want to return this book?
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setReturnDialogOpen(false)} color="primary">
            Cancel
          </Button>
          <Button onClick={handleReturnConfirmation} color="primary">
            Return
          </Button>
        </DialogActions>
      </Dialog>
      {returnMessage && (
        <Typography variant="body2" color={returnMessage.startsWith('Error') ? 'error' : 'success'}>
          {returnMessage}
        </Typography>
      )}
    </div>
  );
};

export default BookReturn;