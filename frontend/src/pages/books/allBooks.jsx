import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axiosInstance from '../../utils/axiosInstance';
import { CircularProgress, Typography, Grid, Card, CardContent, CardMedia, Button } from '@mui/material';

const AllBooks = () => {
  const [bookData, setBookData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
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
          <Grid container spacing={3}>
            {bookData.map((book) => (
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
