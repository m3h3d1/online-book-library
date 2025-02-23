import React, { useState } from 'react';
import axiosInstance from '../utils/axiosInstance';
import { Typography, TextField, Button, Paper, Grid } from '@mui/material';

const UserSearchPage = () => {
  const [userId, setUserId] = useState('');
  const [userData, setUserData] = useState(null);

  const handleUserIdChange = (e) => {
    setUserId(e.target.value);
  };

  const searchUser = () => {
    axiosInstance.get(`/users/${userId}`)
      .then((response) => {
        setUserData(response.data);
      })
      .catch((error) => {
        console.error('Error fetching user data:', error);
      });
  };

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <Paper style={{ padding: 20 }}>
          <Typography variant="h4"><center>Search User by User ID</center></Typography>
          <div>
            <TextField
              label="User ID"
              value={userId}
              onChange={handleUserIdChange}
              variant="outlined"
            />
            <Button variant="contained" color="primary" onClick={searchUser}>
              Search
            </Button>
          </div>
          {userData && (
            <div>
              <Typography variant="h6">User Information</Typography>
              <Typography>User ID: {userData.userId}</Typography>
              <Typography>First Name: {userData.firstName}</Typography>
              <Typography>Last Name: {userData.lastName}</Typography>
              <Typography>Email: {userData.email}</Typography>
              <Typography>Address: {userData.address}</Typography>
              <Typography>Role: {userData.role}</Typography>
            </div>
          )}
        </Paper>
      </Grid>
    </Grid>
  );
};

export default UserSearchPage;
