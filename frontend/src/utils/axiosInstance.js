import axios from "axios";

export const axiosInstance = axios.create({
  // baseURL: import.meta.env.VITE_API_URL, // 
  baseURL: "http://localhost:1313",
  timeout: 3000,
});

axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

// export const axiosInstanceProductService = axios.create({
//   baseURL: "https://localhost:3001",
//   timeout: 3000,
// });

export default axiosInstance;
