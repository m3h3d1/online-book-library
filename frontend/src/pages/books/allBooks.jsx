import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axiosInstance from '../../utils/axiosInstance';
import { CircularProgress, Typography, Grid, Card, CardContent, CardMedia, Button } from '@mui/material';

const AllBooks = () => {
  const [bookData, setBookData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Make an HTTP GET request to fetch book data from the API
    axiosInstance.get('/books/all')
      .then((response) => {
        setBookData(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching book data:', error);
        setLoading(false);
      });
  }, []);

  return (
    <div>
      {loading ? (
        <CircularProgress />
      ) : (
        <div>
          <Typography variant="h4" gutterBottom>
            Book Information
          </Typography>
          <Grid container spacing={3}>
            {bookData.map((book) => (
              <Grid item xs={12} sm={6} md={4} lg={3} key={book.bookId}>
                <Card>
                  <CardMedia
                    component="img"
                    alt={book.title}
                    style={{ height: '100%', width: '100%', objectFit: 'cover' }}
                    image={book.coverUrl}
                  />
                  <CardContent>
                    <Link to={`/books/${book.bookId}`}>
                      <Typography variant="h6" component="div">
                        {book.title}
                      </Typography>
                    </Link>
                    <Typography variant="body2" color="textSecondary">
                      Author: {book.author}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      Availability Status: {book.availabilityStatus}
                    </Typography>
                    <Link to={`/books/${book.bookId}/reviews`}>
                      <Button variant="contained" color="primary">
                        Rating & Reviews
                      </Button>
                    </Link>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </div>
      )}
    </div>
  );
};

export default AllBooks;
