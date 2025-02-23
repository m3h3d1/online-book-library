import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';

const UserSearchPage = () => {
  const [userId, setUserId] = useState('');
  const [userData, setUserData] = useState(null);

  const handleUserIdChange = (e) => {
    setUserId(e.target.value);
  };

  const searchUser = () => {
    axiosInstance.get(`/users/${userId}`)
      .then((response) => {
        setUserData(response.data);
      })
      .catch((error) => {
        console.error('Error fetching user data:', error);
      });
  };

  return (
    <div>
      <h1>Search User by UserId</h1>
      <div>
        <label>
          User ID:
          <input type="text" value={userId} onChange={handleUserIdChange} />
        </label>
        <button onClick={searchUser}>Search</button>
      </div>
      {userData && (
        <div>
          <h2>User Information</h2>
          <p>User ID: {userData.userId}</p>
          <p>First Name: {userData.firstName}</p>
          <p>Last Name: {userData.lastName}</p>
          <p>Email: {userData.email}</p>
          <p>Address: {userData.address}</p>
          <p>Role: {userData.role}</p>
        </div>
      )}
    </div>
  );
};

export default UserSearchPage;
