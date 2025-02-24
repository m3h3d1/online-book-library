import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';
import { Typography, CircularProgress, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@mui/material';
import { Link } from 'react-router-dom';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axiosInstance
      .get('/users')
      .then((response) => {
        setUsers(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching user data:', error);
        setLoading(false);
      });
  }, []);

  const deleteUser = (userId) => {
    axiosInstance
      .delete(`/users/${userId}`)
      .then(() => {
        // Remove the deleted user from the state
        setUsers(users.filter((user) => user.userId !== userId));
      })
      .catch((error) => {
        console.error('Error deleting user:', error);
      });
  };

  return (
    <div>
      <Typography variant="h4" gutterBottom>
        User Information
      </Typography>
      {loading ? (
        <CircularProgress />
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>User ID</TableCell>
                <TableCell>Name</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Address</TableCell>
                <TableCell>Role</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((user) => (
                <TableRow key={user.userId}>
                  <TableCell>{user.userId}</TableCell>
                  <TableCell>
                    <Link to={`/users/${user.userId}`}>{user.firstName} {user.lastName}</Link>
                  </TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>{user.address}</TableCell>
                  <TableCell>{user.role}</TableCell>
                  <TableCell>
                    <Link to={`/users/${user.userId}/history`}>
                      <Button variant="outlined" color="primary">
                        Borrowed Books
                      </Button>
                    </Link>
                    <Link to={`/users/${user.userId}/reserved-books`}>
                      <Button variant="outlined" color="secondary">
                        Reserved Books
                      </Button>
                    </Link>
                    <Button
                      variant="outlined"
                      color="error"
                      onClick={() => deleteUser(user.userId)}
                    >
                      Delete User
                    </Button>
                  </TableCell>
                  <TableCell>
                    
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </div>
  );
};

export default UserList;