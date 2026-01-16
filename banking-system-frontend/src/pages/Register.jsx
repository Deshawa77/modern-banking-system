import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";

const Register = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("USER");
  const [message, setMessage] = useState("");

  const navigate = useNavigate(); // <-- add this

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      await api.post("/auth/register", {
        username,
        password,
        role
      });

      setMessage("User registered successfully");

      
      setUsername("");
      setPassword("");
      setRole("USER");

      
      setTimeout(() => {
        navigate("/login");
      }, 1500);

    } catch (error) {
      setMessage(
        error.response?.data?.message || "Registration failed"
      );
    }
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "400px" }}>
      <h3>Register User</h3>

      <form onSubmit={handleRegister}>
        <input
          className="form-control my-2"
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />

        <input
          className="form-control my-2"
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <select
          className="form-control my-2"
          value={role}
          onChange={(e) => setRole(e.target.value)}
        >
          <option value="USER">USER</option>
          <option value="ADMIN">ADMIN</option>
        </select>

        <button className="btn btn-success w-100" type="submit">
          Register
        </button>
      </form>

      {message && <p className="mt-3" style={{ color: "#50e3c2" }}>{message}</p>}
    </div>
  );
};

export default Register;
