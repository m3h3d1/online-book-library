import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../utils/axiosInstance';
import { useParams } from 'react-router-dom';
import {
  Table,
  TableContainer,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Paper,
  Typography,
} from '@mui/material';
import CancelReservationButton from './CancelReservation';

const UserReservationHistoryPage = () => {
  const [reservationHistory, setReservationHistory] = useState([]);
  const { userId } = useParams();

  useEffect(() => {
    axiosInstance.get(`/users/${userId}/reserved-books`)
      .then((response) => {
        setReservationHistory(response.data);
      })
      .catch((error) => {
        console.error('Error fetching user reservation history:', error);
      });
  }, [userId]);

  const handleCancelReservation = (bookId) => {
    setReservationHistory((prevHistory) => prevHistory.filter((item) => item.id !== bookId));
  };

  return (
    <div>
      <Typography variant="h4"><center>User Reservation History</center></Typography>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><b>Book Title</b></TableCell>
              <TableCell><b>Book Author</b></TableCell>
              <TableCell><b>Reservation Date</b></TableCell>
              <TableCell><b>Reservation Status</b></TableCell>
              <TableCell><b>Action</b></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {reservationHistory.map((item, index) => (
              <TableRow key={index}>
                <TableCell>{item.title}</TableCell>
                <TableCell>{item.author}</TableCell>
                <TableCell>{item.reservationDate}</TableCell>
                <TableCell>{item.reservationStatus}</TableCell>
                <TableCell>
                  {item.reservationStatus === 'RESERVED' && (
                    <CancelReservationButton
                      bookId={item.bookId}
                      onCancelSuccess={handleCancelReservation}
                    />
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};

export default UserReservationHistoryPage;
