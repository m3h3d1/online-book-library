import { Navigate, Outlet } from "react-router-dom";
import {
  getRole,
  isAdmin,
  isCustomer,
  isLogged,
} from "./accountInfo";

export const Authenticate = ({ requiredRole }) => {
  console.log("Required role: " + requiredRole + ", user role: " + getRole());

  if (
    (requiredRole === "ANY" ||
      requiredRole === "CUSTOMER" ||
      requiredRole === "ADMIN") &&
    !isLogged()
  ) {
    return <Navigate to={"/login"} />;
  }

  if (requiredRole === "ADMIN" && isCustomer()) {
    return <Navigate to={"/"} />;
  }

  if (requiredRole === "CUSTOMER" && isAdmin()) {
    return <Navigate to={"/"} />;
  }

  console.log("Serving user the contents.");
  return <Outlet />;
};

export default Authenticate;
