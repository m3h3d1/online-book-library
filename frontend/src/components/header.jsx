import { Link, useNavigate } from "react-router-dom";

const Header = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  return (
    <div>
      {/* <div>Logo</div> */}

      <div style={{ display: "flex", justifyContent: "space-around" }}>
        <Link to="/">Home</Link>
        {token && (
          <>
            <Link to="/users">User List</Link>
            <Link to="/user/search">Search User</Link>
            <Link to="/books/create">Add Book</Link>
          </>
        )}
        {!token && (
          <>
            <Link to="/user/register">Register</Link>
            <Link to="/login">Login</Link>
          </>
        )}

        {token && (
          <button
            onClick={() => {
              localStorage.removeItem("token");
              navigate("/login");
            }}
          >
            Logout
          </button>
        )}
      </div>
    </div>
  );
};

export default Header;
