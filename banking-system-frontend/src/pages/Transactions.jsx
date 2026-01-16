import { useState } from "react";
import api from "../api/axiosConfig";

function Transactions() {
  const [accountId, setAccountId] = useState("");
  const [transactions, setTransactions] = useState([]);

  const loadTransactions = async () => {
    try {
      const res = await api.get(`/account/transactions`, {
        params: { accountId } // ✅ send as query parameter
      });
      setTransactions(res.data);
    } catch (err) {
      if (err.response?.status === 403) {
        alert("Unauthorized. Please login again.");
        localStorage.removeItem("token");
        window.location.href = "/login"; // redirect to login
      } else {
        alert("Failed to load transactions");
      }
    }
  };

  return (
    <div className="container mt-4">
      <h2>Transaction History</h2>

      <input
        className="form-control my-2"
        placeholder="Account ID"
        value={accountId}
        onChange={e => setAccountId(e.target.value)}
      />

      <button className="btn btn-secondary" onClick={loadTransactions}>
        Load Transactions
      </button>

      {transactions.length > 0 && (
        <table className="table table-striped mt-3">
          <thead>
            <tr>
              <th>Type</th>
              <th>Amount</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            {transactions.map(tx => (
              <tr key={tx.id}>
                <td>{tx.type}</td>
                <td>{tx.amount}</td>
                <td>{tx.timestamp}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default Transactions;
