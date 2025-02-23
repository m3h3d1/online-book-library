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
import UserHistoryPage from "./pages/UserHistoryPage";
import AdminPage from "./pages/AdminPage";
// import SignIn from "./pages/SignIn";

function App() {
  return (
    <div>
      <Header />
      <Routes>
        <Route path="/" exact element={<HomePage />} />
        <Route path="/user/register" element={<RegistrationPage />} />
        <Route path="/login" element={<LoginPage />} />
        {/* <Route path="/signin" element={<SignIn />} /> */}
        <Route path="/books/:bookId" element={<BookDetailsPage/>} />
        <Route element={<Authenticate />}>
          <Route path="/books/create" element={<AddBookPage />} />
          <Route path="/users" element={<UserList />} />
          <Route path="/users/:userId" element={<UserDetails />} />
          <Route path="/users/search/:userId" element={<SearchPage />} />
          <Route path="/users/:userId/history" element={<UserHistoryPage />} />
          <Route path="/admin" element={<AdminPage />} />
        </Route>
        <Route path="/books/:bookId/reviews" element={<BookReviewsPage/>} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      <Footer />
    </div>
  );
}

export default App;
