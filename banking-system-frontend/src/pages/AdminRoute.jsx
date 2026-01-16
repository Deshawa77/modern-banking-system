import { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

function AdminRoute({ children }) {
  const { token, role } = useContext(AuthContext);

  // normalize role to uppercase for safe comparison
  if (!token || !role || role.toUpperCase() !== "ADMIN") {
    return <Navigate to="/login" replace />;
  }

  return children;
}

export default AdminRoute;
