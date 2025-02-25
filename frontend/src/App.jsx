import { Route, Routes } from "react-router-dom";
import "./App.scss";
import HomePage from "./pages/homePage";
import RegistrationPage from "./pages/registrationPage";
import LoginPage from "./pages/loginPage";
import Header from "./components/common/header";
import NotFoundPage from "./pages/notFoundPage";
import UserList from "./components/UserList";
import UserDetails from "./pages/userDetailsPage";
import Authenticate from "./components/authenticate";
import SearchPage from "./pages/searchPage";
import AddBookPage from "./pages/AddBookPage";
import BookDetailsPage from "./pages/books/bookDetailsPage";
import Footer from "./components/common/footer";
import BookReviewsPage from "./pages/reviews/BookReviewsPage";
import AdminPage from "./pages/AdminPage";
import UserReservationHistoryPage from "./pages/books/reserve/UserReservationHistoryPage";
import SearchBooksPage from "./pages/books/SearchBooksPage";
import UserHistoryPage from "./pages/UserBorrowingHistoryPage";

function App() {
  
  return (
    <div>
      <Header />
      <Routes>
        <Route path="/" exact element={<HomePage />} />
        <Route path="/user/register" element={<RegistrationPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route element={<Authenticate requiredRole={"ANY"} />}>
          <Route path="/users/:userId" element={<UserDetails />} />
          <Route path="/users/:userId/history" element={<UserHistoryPage />} />
          <Route path="/books/:bookId/reviews" element={<BookReviewsPage/>} />
          <Route path="/books/:bookId" element={<BookDetailsPage/>} />
          <Route path="/users/:userId/reserved-books" element={<UserReservationHistoryPage />} />
          <Route path="/books/search" element={<SearchBooksPage />} />
        </Route>
        <Route element={<Authenticate requiredRole={"ADMIN"} />} >
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/users/search/:userId" element={<SearchPage />} />
          <Route path="/users" element={<UserList />} />
          <Route path="/books/create" element={<AddBookPage />} />
        </Route>
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      <Footer />
    </div>
  );
}

export default App;
