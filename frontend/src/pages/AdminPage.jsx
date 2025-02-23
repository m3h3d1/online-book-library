import React from 'react';
import { Link } from 'react-router-dom';
import { Button, Container, Typography } from '@mui/material';

const AdminPage = () => {
  return (
    <Container>
      <Typography variant="h4" gutterBottom>
        Admin Page
      </Typography>
      <Button
        variant="contained"
        color="primary"
        component={Link}
        to="/users"
        style={{ margin: '10px' }}
      >
        User List
      </Button>
      <Button
        variant="contained"
        color="primary"
        component={Link}
        to="/books/create"
        style={{ margin: '10px' }}
      >
        Add Books
      </Button>
        
        <Button
        variant="contained"
        color="primary"
        component={Link}
        to="/users/search/:userId"
        style={{ margin: '10px' }}
      >
        Search User
      </Button>
    </Container>
  );
};

export default AdminPage;