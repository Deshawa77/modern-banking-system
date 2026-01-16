import { useState } from "react";
import api from "../api/axiosConfig";

function Balance() {
  const [accountId, setAccountId] = useState("");
  const [balance, setBalance] = useState(null);

  // ✅ Updated function
  const checkBalance = async () => {
    try {
      const res = await api.get(`/account/balance`, {
        params: { accountId } // send as query parameter
      });
      setBalance(res.data.balance);
    } catch (err) {
      if (err.response?.status === 403) {
        alert("Unauthorized. Please login again.");
        localStorage.removeItem("token");
        window.location.href = "/login"; // redirect to login
      } else {
        alert("Error fetching balance");
      }
    }
  };

  return (
    <div className="container mt-4">
      <h2>Balance Inquiry</h2>

      <input
        className="form-control my-2"
        placeholder="Account ID"
        value={accountId}
        onChange={e => setAccountId(e.target.value)}
      />

      <button className="btn btn-primary" onClick={checkBalance}>
        Check Balance
      </button>

      {balance !== null && (
        <h4 className="mt-3">Balance: {balance}</h4>
      )}
    </div>
  );
}

export default Balance;
