import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';
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

const UserHistoryPage = () => {
  const [history, setHistory] = useState([]);
  const { userId } = useParams();

  useEffect(() => {
    axiosInstance.get(`/users/${userId}/history`)
      .then((response) => {
        setHistory(response.data);
      })
      .catch((error) => {
        console.error('Error fetching user history:', error);
      });
  }, [userId]);

  return (
    <div>
      <Typography variant="h4">User Borrowing History</Typography>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Book Title</TableCell>
              <TableCell>Due Date</TableCell>
              <TableCell>Return Date</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {history.map((item, index) => (
              <TableRow key={index}>
                <TableCell>{item.bookTitle}</TableCell>
                <TableCell>{item.dueDate}</TableCell>
                <TableCell>{item.returnDate || 'Not returned yet'}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};

export default UserHistoryPage;