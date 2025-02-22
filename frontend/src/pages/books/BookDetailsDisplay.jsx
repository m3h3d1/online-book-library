import React from 'react';
import { Typography, Button, Card, CardContent, CardMedia, Grid, TextField } from '@mui/material';

const BookDetailsDisplay = ({ bookData, editMode, updatedBookData, handleEditClick, handleUpdateClick }) => {
  return (
    <Grid container spacing={3}>
      <Grid item xs={12} sm={6}>
        <Card>
          <CardMedia
            component="img"
            alt={bookData.title}
            style={{ height: '100%', width: '100%', objectFit: 'cover' }}
            image={bookData.coverUrl}
          />
        </Card>
      </Grid>
      <Grid item xs={12} sm={6}>
        <Card>
          <CardContent>
            {editMode ? (
              <div>
                <TextField
                  label="Title"
                  fullWidth
                  value={updatedBookData.title}
                  onChange={(e) => handleUpdateClick({ title: e.target.value })}
                />
                <TextField
                  label="Author"
                  fullWidth
                  value={updatedBookData.author}
                  onChange={(e) => handleUpdateClick({ author: e.target.value })}
                />
              </div>
            ) : (
              <div>
                <Typography variant="h6" component="div">
                  {bookData.title}
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  Author: {bookData.author}
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  Availability Status: {bookData.availabilityStatus}
                </Typography>
              </div>
            )}
            {editMode ? (
              <Button variant="contained" color="primary" onClick={handleUpdateClick}>
                Confirm Update
              </Button>
            ) : (
              <div>
                <Button variant="contained" color="primary" onClick={handleEditClick}>
                  Update Book
                </Button>
              </div>
            )}
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  );
};

export default BookDetailsDisplay;