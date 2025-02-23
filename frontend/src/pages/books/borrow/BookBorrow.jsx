import React, { useState } from 'react';
import axiosInstance from '../../../utils/axiosInstance';
import { Button, TextField, Dialog, DialogTitle, DialogContent, DialogActions, Typography } from '@mui/material';

const BookBorrow = ({ bookId, onBorrowSuccess }) => {
  const [dueDate, setDueDate] = useState('');
  const [borrowDialogOpen, setBorrowDialogOpen] = useState(false);
  const [borrowMessage, setBorrowMessage] = useState('');

  const handleBorrowClick = () => {
    setBorrowDialogOpen(true);
  };

  const handleBorrowConfirmation = () => {
    axiosInstance
      .post(`/books/${bookId}/borrow`, { dueDate })
      .then((response) => {
        console.log('Book borrowed successfully');
        setBorrowMessage('Book borrowed successfully');
        onBorrowSuccess(response.data);
        setBorrowDialogOpen(false);
      })
      .catch((error) => {
        console.error('Error borrowing book:', error);
        setBorrowMessage('Error borrowing book. Please try again.');
        setBorrowDialogOpen(false);
      });
  };

  return (
    <div>
      <Button variant="contained" color="primary" onClick={handleBorrowClick}>
        Borrow Book
      </Button>
      <Dialog open={borrowDialogOpen} onClose={() => setBorrowDialogOpen(false)}>
        <DialogTitle>Borrow Book</DialogTitle>
        <DialogContent>
          <TextField
            label="Due Date"
            type="date"
            value={dueDate}
            onChange={(e) => setDueDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setBorrowDialogOpen(false)} color="primary">
            Cancel
          </Button>
          <Button onClick={handleBorrowConfirmation} color="primary">
            Borrow
          </Button>
        </DialogActions>
      </Dialog>
      {borrowMessage && (
        <Typography variant="body2" color={borrowMessage.startsWith('Error') ? 'error' : 'success'}>
          {borrowMessage}
        </Typography>
      )}
    </div>
  );
};

export default BookBorrow;
