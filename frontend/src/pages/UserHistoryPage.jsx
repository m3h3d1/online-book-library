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
import BookReturn from './books/borrow/BookReturn';

const UserHistoryPage = () => {
  const [history, setHistory] = useState([]);
  const { userId } = useParams();
  const [bookData, setBookData] = useState(null);

  useEffect(() => {
    axiosInstance.get(`/users/${userId}/history`)
      .then((response) => {
        setHistory(response.data);
      })
      .catch((error) => {
        console.error('Error fetching user history:', error);
      });
  }, [userId]);

  const handleReturnSuccess = (returnedBook) => {
    setBookData(returnedBook);
  };

  return (
    <div>
      <Typography variant="h4"><center>User Borrowing History</center></Typography>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><b>Book Title</b></TableCell>
              <TableCell><b>Due Date</b></TableCell>
              <TableCell><b>Return Date</b></TableCell>
              <TableCell><b>Action</b></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {history.map((item, index) => (
              <TableRow key={index}>
                <TableCell>{item.bookTitle}</TableCell>
                <TableCell>{item.dueDate}</TableCell>
                <TableCell>{item.returnDate || 'Not returned yet'}</TableCell>
                <TableCell>
                  {item.returnDate === null ? (
                    <BookReturn bookId={item.bookId} onReturnSuccess={handleReturnSuccess} />
                  ) : (
                    'Book Returned'
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

export default UserHistoryPage;