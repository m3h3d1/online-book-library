import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axiosInstance from "../utils/axiosInstance";

const UserDetails = () => {
  const { userId } = useParams();
  const [userDetails, setUserDetails] = useState();

  useEffect(() => {
    axiosInstance.get(`/users/${userId}`).then((resp) => {
      const data = resp.data;
      setUserDetails(data.data);
    });
  }, []);

  return (
    <div
      style={{ display: "flex", flexDirection: "column", alignItems: "center" }}
    >
      <h1>User details of ID: {userId}</h1>
      <div>
        <h3>{userDetails?.first_name}</h3>
        <img src={userDetails?.avatar} />
      </div>
    </div>
  );
};

export default UserDetails;
