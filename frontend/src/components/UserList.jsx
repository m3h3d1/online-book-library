import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axiosInstance
      .get('/users')
      .then((response) => {
        setUsers(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching user data:', error);
        setLoading(false);
      });
  }, []);

  return (
    <div>
      <h2>User Information</h2>
      {loading ? (
        <p>Loading users...</p>
      ) : (
        <ul>
          {users.map((user) => (
            <li key={user.userId}>
              <p>User ID: {user.userId}</p>
              <p>Name: {user.firstName} {user.lastName}</p>
              <p>Email: {user.email}</p>
              <p>Address: {user.address}</p>
              <p>Role: {user.role}</p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default UserList;
