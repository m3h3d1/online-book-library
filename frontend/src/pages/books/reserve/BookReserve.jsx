import React, { useState } from 'react';
import { Button, Dialog, DialogTitle, DialogContent, DialogActions, Typography } from '@mui/material';
import axiosInstance from '../../../utils/axiosInstance';

const BookReserve = ({ bookId, onReserveSuccess }) => {
  const [reserveDialogOpen, setReserveDialogOpen] = useState(false);
  const [reserving, setReserving] = useState(false);
  const [reservationError, setReservationError] = useState(null);

  const handleReserveClick = () => {
    setReserveDialogOpen(true);
  };

  const handleConfirmReservation = () => {
    setReserving(true);
    setReservationError(null);

    axiosInstance.post(`/books/${bookId}/reserve`)
      .then((response) => {
        console.log('Book reserved:', response.data);
        setReserveDialogOpen(false);
        onReserveSuccess(response.data); // Notify the parent component of the reservation success
      })
      .catch((error) => {
        console.error('Error reserving book:', error);
        setReserving(false);
        setReservationError('Error reserving the book. Please try again later.');
      });
  };

  return (
    <div>
      <Button variant="contained" color="primary" onClick={handleReserveClick}>
        Reserve Book
      </Button>

      <Dialog open={reserveDialogOpen} onClose={() => setReserveDialogOpen(false)}>
        <DialogTitle>Confirm Reservation</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to reserve this book?
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setReserveDialogOpen(false)} color="primary">
            Cancel
          </Button>
          <Button onClick={handleConfirmReservation} color="primary" disabled={reserving}>
            {reserving ? 'Reserving...' : 'Reserve'}
          </Button>
        </DialogActions>
        {reservationError && (
          <Typography color="error" variant="body2" style={{ padding: '16px' }}>
            {reservationError}
          </Typography>
        )}
      </Dialog>
    </div>
  );
};

export default BookReserve;
