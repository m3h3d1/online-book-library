import React, { useState, useEffect } from 'react';
import axiosInstance from '../../utils/axiosInstance';
import { CircularProgress, Typography, Grid, Card, CardContent, CardMedia, Button, TextField } from '@mui/material';

const SearchBooksPage = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [loading, setLoading] = useState(false);

  const handleSearch = () => {
    setLoading(true);
    axiosInstance.get('/books/search', { title: searchQuery })
      .then((response) => {
        setSearchResults(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error searching for books:', error);
        setLoading(false);
      });
  };

  return (
    <div>
      <Typography variant="h4">Search Books</Typography>
      <TextField
        label="Search by Title"
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
      />
      <Button variant="contained" color="primary" onClick={handleSearch}>
        Search
      </Button>
      {loading ? (
        <CircularProgress />
      ) : (
        <Grid container spacing={3}>
          {searchResults.map((book) => (
            <Grid item xs={12} sm={6} md={4} lg={3} key={book.bookId}>
              <Card style={{ height: '600px', display: 'flex', flexDirection: 'column' }}>
                <CardMedia
                  component="img"
                  alt={book.title}
                  height={"400px"}
                  style={{ flex: 1, objectFit: 'cover' }}
                  image={book.coverUrl}
                />
                <CardContent style={{ flex: 1 }}>
                  <Typography variant="h6" component="div">
                    {book.title}
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    Author: {book.author}
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    Availability Status: {book.availabilityStatus}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </div>
  );
};

export default SearchBooksPage;
