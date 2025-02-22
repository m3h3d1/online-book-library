import UserList from "../components/UserList";
import AllBooks from "./books/allBooks";

const HomePage = () => {
  return (
    <div>
      <h1 style={{ textAlign: "center" }}>Home page</h1>
      <AllBooks/>

      {/* <UserList /> */}
    </div>
  );
};

export default HomePage;
