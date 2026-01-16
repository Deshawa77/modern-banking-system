import { useEffect, useState } from "react";
import axios from "axios";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from "recharts";

function AdminDashboard() {
  const [analytics, setAnalytics] = useState({
    totalDeposits: 0,
    totalWithdrawals: 0,
    highValueTransactions: 0,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchAnalytics = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        setError("No token found. Please login as Admin.");
        setLoading(false);
        return;
      }

      try {
        const res = await axios.get("http://localhost:8080/admin/analytics/summary", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setAnalytics(res.data);
      } catch (err) {
        console.error(err);
        setError("Failed to fetch analytics. Are you an Admin?");
      } finally {
        setLoading(false);
      }
    };

    fetchAnalytics();
  }, []);

  if (loading) return <p>Loading analytics...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;

  
  const chartData = [
    { name: "Deposits", amount: analytics.totalDeposits },
    { name: "Withdrawals", amount: analytics.totalWithdrawals },
  ];

  return (
    <div style={{ padding: "2rem" }}>
      <h2>Admin Transaction Analytics Dashboard</h2>

      <div style={{ display: "flex", gap: "1rem", margin: "2rem 0" }}>
        <div style={cardStyle}>
          <h3>Total Deposits</h3>
          <p>₨ {analytics.totalDeposits.toLocaleString()}</p>
        </div>

        <div style={cardStyle}>
          <h3>Total Withdrawals</h3>
          <p>₨ {analytics.totalWithdrawals.toLocaleString()}</p>
        </div>

        <div style={cardStyle}>
          <h3>High-Value Transactions</h3>
          <p>{analytics.highValueTransactions}</p>
        </div>
      </div>

      <h3>Deposits vs Withdrawals</h3>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={chartData}>
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="amount" fill="#4f46e5" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}


const cardStyle = {
  flex: 1,
  padding: "1rem",
  background: "#1c1c1e", 
  color: "#ffffff",       
  borderRadius: "0.5rem",
  boxShadow: "0 2px 5px rgba(0,0,0,0.5)",
  textAlign: "center",
};


export default AdminDashboard;
