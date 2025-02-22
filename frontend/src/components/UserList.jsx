import { useNavigate } from "react-router-dom";
import useUserHook from "../hooks/useUserHook";

const UserList = () => {
  const navigate = useNavigate();
  const { users, handleSubmit, errors } = useUserHook();

  return (
    <div
      style={{ display: "flex", flexDirection: "column", alignItems: "center" }}
    >
      <h1>UserList</h1>
      {users &&
        users.map((user, i) => {
          return (
            <div key={i}>
              <h2>{user.first_name}</h2>
              <p>Email: {user.email}</p>
              <button onClick={() => navigate(`/users/${user.id}`)}>
                Details
              </button>
            </div>
          );
        })}
    </div>
  );
};

export default UserList;
