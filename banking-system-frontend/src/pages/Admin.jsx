import { useState } from "react";
import api from "../api/axiosConfig";

function Admin() {
  const [userId, setUserId] = useState("");
  const [loading, setLoading] = useState(false);

  const createAccount = async () => {
    if (!userId) {
      alert("Please enter a User ID");
      return;
    }

    try {
      setLoading(true);

      await api.post("/account/create", {
        userId: Number(userId)
      });

      alert("Account created successfully");
      setUserId("");
    } catch (error) {
      console.error("Create account error:", error);
      alert("Only ADMIN can do this or request is invalid");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <h2>Admin Panel</h2>

      <input
        type="number"
        className="form-control my-2"
        placeholder="User ID"
        value={userId}
        onChange={(e) => setUserId(e.target.value)}
      />

      <button
        className="btn btn-warning"
        onClick={createAccount}
        disabled={loading}
      >
        {loading ? "Creating..." : "Create Account"}
      </button>
    </div>
  );
}

export default Admin;
