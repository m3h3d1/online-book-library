import axios from "axios";
import { useState } from "react";
import axiosInstance from "../utils/axiosInstance";
import { useNavigate } from "react-router-dom";

const AddBookPage = () => {
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [author, setAuthor] = useState("");
  const [coverUrl, setCoverUrl] = useState("");
  const [isBookAdded, setIsBookAdded] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState();

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
        console.log("Error ", error);
        setError(error);
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <div
      style={{ display: "flex", flexDirection: "column", alignItems: "center" }}
    >
      <h1>Add a Book</h1>
      {isBookAdded && (
        <h2 style={{ color: "green" }}>Book Added Successfully</h2>
      )}
      {isLoading && <h1>Loading.....</h1>}
      <form onSubmit={handleAddBook}>
        <div>
          <h4>Title</h4>
          <input
            value={title}
            placeholder="Enter Title"
            onChange={(e) => {
              setTitle(e.target.value);
            }}
          />
        </div>

        <div>
          <h4>Author</h4>
          <input
            value={author}
            placeholder="Enter Author"
            onChange={(e) => {
              setAuthor(e.target.value);
            }}
          />
        </div>

        <div>
          <h4>Cover URL</h4>
          <input
            value={coverUrl}
            placeholder="Enter Cover URL"
            onChange={(e) => {
              setCoverUrl(e.target.value);
            }}
          />
        </div>

        <button type="submit">Add Book</button>
      </form>
    </div>
  );
};

export default AddBookPage;
