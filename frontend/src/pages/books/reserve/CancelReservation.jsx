import React from 'react';
import axiosInstance from '../../../utils/axiosInstance';
import { Button } from '@mui/material';

const CancelReservationButton = ({ bookId, onCancelSuccess }) => {
  const handleCancelReservation = () => {
    axiosInstance.delete(`/books/${bookId}/cancel-reservation`)
      .then(() => {
        onCancelSuccess(bookId);
      })
      .catch((error) => {
        console.error('Error canceling reservation:', error);
      });
  };

  return (
    <Button variant="contained" color="error" onClick={handleCancelReservation}>
      Cancel Reservation
    </Button>
  );
};

export default CancelReservationButton;
