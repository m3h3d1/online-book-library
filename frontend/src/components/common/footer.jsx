import React from 'react';
import { AppBar, Toolbar, Typography } from '@mui/material';

const Footer = () => {
  return (
    <AppBar position="static" color="primary">
      <Toolbar>
        <Typography variant="body2" color="inherit" align="center">
          &copy; 2023 - OBLA. All rights reserved.
        </Typography>
      </Toolbar>
    </AppBar>
  );
};

export default Footer;
