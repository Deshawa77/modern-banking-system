import { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

function ProtectedRoute({ children, requiredRole }) {
  const { token, role } = useContext(AuthContext);

  if (!token) {
    return <Navigate to="/login" />;
  }

  if (requiredRole && role !== requiredRole) {
    return <Navigate to="/dashboard" />;
  }

  return children;
}

export default ProtectedRoute;
