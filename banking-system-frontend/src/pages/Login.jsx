import { useState, useContext } from "react";
import api from "../api/axiosConfig";
import { AuthContext } from "../context/AuthContext";
import { useNavigate, Link } from "react-router-dom";

function Login() {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post("/auth/login", {
        username,
        password,
      });

      
      const { token, role } = res.data;

      login(token, role); 

      alert("Login successful");
      
      
      if (role === "ADMIN") {
        navigate("/admin");
      } else {
        navigate("/dashboard");
      }

    } catch (err) {
      alert("Invalid credentials");
      console.error(err);
    }
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "400px" }}>
      <h3>Login</h3>

      <form onSubmit={submit}>
        <input
          className="form-control my-2"
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

        <button className="btn btn-primary w-100 mt-2">
          Login
        </button>
      </form>

      <p className="mt-3" style={{ color: "#50e3c2" }}>
        Don't have an account? <Link to="/register">Register here</Link>
      </p>
    </div>
  );
}

export default Login;
