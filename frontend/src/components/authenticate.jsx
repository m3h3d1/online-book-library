import React from "react";
import { Navigate, Outlet } from "react-router-dom";

const Authenticate = () => {
  const token = localStorage.getItem("token");
  console.log("token is ", token);

  return <div>{token ? <Outlet /> : <Navigate to="/login" />}</div>;
};

export default Authenticate;
