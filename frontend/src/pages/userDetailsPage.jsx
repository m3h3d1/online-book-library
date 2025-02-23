import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axiosInstance from "../utils/axiosInstance";
import { Typography, Paper, Grid, CircularProgress } from "@mui/material";

const UserDetails = () => {
  const { userId } = useParams();
  const [userDetails, setUserDetails] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axiosInstance.get(`/users/${userId}`).then((resp) => {
      setUserDetails(resp.data);
      setLoading(false);
    });
  }, [userId]);

  return (
    <Grid container justify="center">
      <Grid item xs={10}>
        <Paper elevation={3} style={{ padding: "20px", margin: "20px" }}>
          <Typography variant="h4" gutterBottom>
            User details of ID: {userId}
          </Typography>
          {loading ? (
            <CircularProgress />
          ) : userDetails ? (
            <div>
              <Typography variant="h6" gutterBottom>
                <b>Name: </b> &nbsp;&nbsp; {userDetails.firstName} {userDetails.lastName}
              </Typography>
              <Typography variant="body1" gutterBottom>
                <b>Email: </b> &nbsp; &nbsp; &nbsp; {userDetails.email}
              </Typography>
              <Typography variant="body1" gutterBottom>
                <b>Address: </b> &nbsp; {userDetails.address}
              </Typography>
              <Typography variant="body1" gutterBottom>
                <b>Role: </b> &nbsp; &nbsp; &nbsp; &nbsp; {userDetails.role}
              </Typography>
            </div>
          ) : (
            <Typography variant="body1">User details not found.</Typography>
          )}
        </Paper>
      </Grid>
    </Grid>
  );
};

export default UserDetails;
