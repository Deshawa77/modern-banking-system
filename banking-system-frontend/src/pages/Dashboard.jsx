import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

function Dashboard() {
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchAccounts = async () => {
      try {
        const res = await api.get("/account/my-accounts");

      
        if (Array.isArray(res.data)) {
          setAccounts(res.data);
        } else if (res.data.accounts) {
        
          setAccounts(res.data.accounts);
        } else {
          setAccounts([]);
          console.warn("Unexpected response format:", res.data);
        }

      } catch (err) {
        console.error("Error fetching accounts:", err);

        if (err.response && err.response.status === 403) {
          alert("Session expired or unauthorized. Please login again.");
          localStorage.removeItem("token");
          window.location.href = "/login"; 
        } else {
          setError("Failed to load accounts. Check console for details.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchAccounts();
  }, []);

  if (loading) {
    return <p className="text-center mt-4">Loading accounts...</p>;
  }

  if (error) {
    return <p className="text-center mt-4 text-danger">{error}</p>;
  }

  return (
    <div className="container mt-4">
      <h2>Dashboard</h2>

      {accounts.length === 0 ? (
        <p>No accounts found.</p>
      ) : (
        <table className="table table-bordered mt-3">
          <thead>
            <tr>
              <th>Account ID</th>
              <th>Account Number</th>
              <th>Balance</th>
            </tr>
          </thead>
          <tbody>
            {accounts.map((acc) => (
              <tr key={acc.id}>
                <td>{acc.id}</td>
                <td>{acc.accountNumber}</td>
                <td>{acc.balance}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default Dashboard;
