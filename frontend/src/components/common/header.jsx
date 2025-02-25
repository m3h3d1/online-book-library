import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Button,
  Typography,
} from '@mui/material';

const Header = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const userId = localStorage.getItem('userId');
  const role = localStorage.getItem('role');

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Online Book Library
        </Typography>
        <div>
          <Button variant="contained" color="primary" component={Link} to="/">
            Home
          </Button>
          <Button variant="contained" color="primary" component={Link} to="/books/search">
          Search Books
          </Button>
          {token ? (
            <> 
              {
                role==="CUSTOMER" && (
                  <>
                    <Button variant="contained" color="primary" component={Link} to={`/users/${userId}/history`}>
                      Borrowed Books
                    </Button>
                    <Button variant="contained" color="primary" component={Link} to={`/users/${userId}/reserved-books`}>
                      Reserved Books
                    </Button>
                  </>
                )
              }

              {
                role==="ADMIN" && (
                  <>
                    <Button variant="contained" color="primary" component={Link} to={`/admin`}>
                      Admin
                    </Button>
                  </>
                )
              }
              
              <Button variant="contained" color="primary" onClick={handleLogout}>
                Logout
              </Button>
            </>
          ) : (
            <>
              <Button variant="contained" color="primary" component={Link} to="/user/register">
                Register
              </Button>
              <Button variant="contained" color="primary" component={Link} to="/login">
                Login
              </Button>
            </>
          )}
        </div>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
