const isLogged = () => {
    const token = localStorage.getItem("token");
    return !!token;
  };
  
  const getRole = () => {
    return localStorage.getItem("role");
  };
  
  const isAdmin = () => {
    const role = localStorage.getItem("role");
    return role === "ADMIN";
  };
  
  const isCustomer = () => {
    const role = localStorage.getItem("role");
    return role === "CUSTOMER";
  };
  
  const getEmail = () => {
    return localStorage.getItem("email");
  };
  
  const clearInfo = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("email");
  };
  
  export { isLogged, isAdmin, isCustomer, getEmail, clearInfo, getRole 
};