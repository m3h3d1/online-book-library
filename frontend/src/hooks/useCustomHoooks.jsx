import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../utils/axiosInstance";

const UseCustomHooks = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const navigate = useNavigate();

  const [isRegistrationDone, setIsRegistrationDone] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const data = {
      email: formData.email,
      password: formData.password,
    };

    console.log(data);

    setIsLoading(true);
    axiosInstance
      .post("/user/login", data)
      .then((resp) => {
        console.log("The Response", resp);
        console.log("Response from login ", data);
        const token = resp.data.Authorization;
        localStorage.setItem("token", token);
        setIsRegistrationDone(true);
        navigate("/"); // Use navigate to redirect to the login page
      })
      .catch((error) => {
        setError(true);
        if (error.response) {
          const errorMessage = error.response.data.message;
          console.log("Error Response Data:", errorMessage);
        } else {
          // Handle other types of errors
        }
        setTimeout(() => {
          setError(false);
        }, 2000);
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return {
    handleChange,
    handleSubmit,
    error,
    formData,
    isLoading,
  };
};

export default UseCustomHooks;