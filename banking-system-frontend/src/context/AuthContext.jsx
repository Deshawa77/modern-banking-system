import { createContext, useState } from "react";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem("token") || "");
  const [role, setRole] = useState(localStorage.getItem("role") || "");

  const login = (tokenValue, roleValue = "") => {
    setToken(tokenValue);
    setRole(roleValue);

    localStorage.setItem("token", tokenValue);
    localStorage.setItem("role", roleValue);
  };

  const logout = () => {
    setToken("");
    setRole("");

    localStorage.removeItem("token");
    localStorage.removeItem("role");
  };

  return (
    <AuthContext.Provider value={{ token, role, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
