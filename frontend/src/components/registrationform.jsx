import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Typography,
  TextField,
  Button,
  Box,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
} from "@mui/material";
import axiosInstance from "../utils/axiosInstance";

const RegistrationForm = () => {
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [address, setAddress] = useState("");
  const [role, setRole] = useState("CUSTOMER"); // Default role is CUSTOMER
  const [isRegistrationDone, setIsRegistrationDone] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  const handleRegister = (e) => {
    e.preventDefault();

    const data = {
      firstName: firstName,
      lastName: lastName,
      email: email,
      password: password,
      address: address,
      role: role,
    };

    setIsLoading(true);

    axiosInstance
      .post("/user/register", data)
      .then((resp) => {
        console.log("The Response", resp);
        setIsRegistrationDone(true);
        navigate("/login");
      })
      .catch((err) => {
        console.error("Error", err);
        setError("An error occurred during registration.");
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      p={3}
      bgcolor="background.paper"
    >
      <Typography variant="h4">Registration</Typography>
      {isRegistrationDone && (
        <Typography style={{ color: "green" }}>
          Successfully Done Registration
        </Typography>
      )}
      {isLoading && <Typography>Loading...</Typography>}
      {error && <Typography color="error">{error}</Typography>}

      <form onSubmit={handleRegister}>
        <TextField
          label="First Name"
          variant="outlined"
          value={firstName}
          placeholder="Enter First Name"
          onChange={(e) => setFirstName(e.target.value)}
          fullWidth
          margin="normal"
        />

        <TextField
          label="Last Name"
          variant="outlined"
          value={lastName}
          placeholder="Enter Last Name"
          onChange={(e) => setLastName(e.target.value)}
          fullWidth
          margin="normal"
        />

        <TextField
          label="Email"
          variant="outlined"
          value={email}
          placeholder="Enter Email"
          onChange={(e) => setEmail(e.target.value)}
          fullWidth
          margin="normal"
        />

        <TextField
          label="Password"
          variant="outlined"
          value={password}
          placeholder="Enter Password"
          type="password"
          onChange={(e) => setPassword(e.target.value)}
          fullWidth
          margin="normal"
        />

        <TextField
          label="Address"
          variant="outlined"
          value={address}
          placeholder="Enter Address"
          onChange={(e) => setAddress(e.target.value)}
          fullWidth
          margin="normal"
        />

        <FormControl variant="outlined" fullWidth margin="normal">
          <InputLabel id="role-label">Role</InputLabel>
          <Select
            labelId="role-label"
            id="role"
            value={role}
            onChange={(e) => setRole(e.target.value)}
            label="Role"
          >
            <MenuItem value="CUSTOMER">Customer</MenuItem>
            <MenuItem value="ADMIN">Admin</MenuItem>
          </Select>
        </FormControl>

        <Button
          type="submit"
          variant="contained"
          color="primary"
          size="large"
          fullWidth
        >
          Register
        </Button>
      </form>
    </Box>
  );
};

export default RegistrationForm;
