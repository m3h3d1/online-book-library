import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Typography,
  TextField,
  Button,
  Box,
} from "@mui/material";
import axiosInstance from "../utils/axiosInstance";

const AddBookPage = () => {
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [author, setAuthor] = useState("");
  const [coverUrl, setCoverUrl] = useState("");
  const [isBookAdded, setIsBookAdded] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  const handleAddBook = (e) => {
    e.preventDefault();

    const data = {
      title: title,
      author: author,
      coverUrl: coverUrl,
    };

    setIsLoading(true);

    axiosInstance
      .post("/books/create", data)
      .then((resp) => {
        console.log("The Response", resp);
        setIsBookAdded(true);
        navigate("/books");
      })
      .catch((error) => {
        console.log("Error", error);
        setError(error);
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <Box
      alignItems="center"
      p={3}
      bgcolor="background.paper"
    >
      <Typography variant="h4">Add a Book</Typography>
      {isBookAdded && (
        <Typography style={{ color: "green" }}>Book Added Successfully</Typography>
      )}
      {isLoading && <Typography>Loading...</Typography>}
      {error && <Typography color="error">{error.message}</Typography>}

      <form onSubmit={handleAddBook}>
        <TextField
          label="Title"
          variant="outlined"
          value={title}
          placeholder="Enter Title"
          onChange={(e) => setTitle(e.target.value)}
          fullWidth
          margin="normal"
        />

        <TextField
          label="Author"
          variant="outlined"
          value={author}
          placeholder="Enter Author"
          onChange={(e) => setAuthor(e.target.value)}
          fullWidth
          margin="normal"
        />

        <TextField
          label="Cover URL"
          variant="outlined"
          value={coverUrl}
          placeholder="Enter Cover URL"
          onChange={(e) => setCoverUrl(e.target.value)}
          fullWidth
          margin="normal"
        />

        <Button
          type="submit"
          variant="contained"
          color="primary"
          size="large"
          fullWidth
        >
          Add Book
        </Button>
      </form>
    </Box>
  );
};

export default AddBookPage;
