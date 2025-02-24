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
          <Button color="inherit" component={Link} to="/">
            Home
          </Button>
          {token ? (
            <> 
              {
                role==="CUSTOMER" && (
                  <>
                    <Button color="inherit" component={Link} to={`/users/${userId}/history`}>
                      Borrowed Books
                    </Button>
                    <Button color="inherit" component={Link} to={`/users/${userId}/reserved-books`}>
                      Reserved Books
                    </Button>
                  </>
                )
              }

              {
                role==="ADMIN" && (
                  <>
                    <Button color="inherit" component={Link} to={`/admin`}>
                      Admin
                    </Button>
                  </>
                )
              }
              
              <Button color="inherit" onClick={handleLogout}>
                Logout
              </Button>
            </>
          ) : (
            <>
              <Button color="inherit" component={Link} to="/user/register">
                Register
              </Button>
              <Button color="inherit" component={Link} to="/login">
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
