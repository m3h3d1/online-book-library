import React from 'react';
import { AppBar, Toolbar, Typography } from '@mui/material';

const styles = {
  appBar: {
    top: 'auto',
    bottom: 0,
    position: 'fixed',
    width: '100%',
  },
};

const Footer = () => {
  return (
    <AppBar style={styles.appBar} color="primary">
      <Toolbar>
        <Typography variant="body2" color="inherit" align="center">
          &copy; 2023 - OBLA. All rights reserved.
        </Typography>
      </Toolbar>
    </AppBar>
  );
};

export default Footer;
