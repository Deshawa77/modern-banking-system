import { useState } from "react";
import Admin from "./Admin"; // Account Management
import AdminDashboard from "./AdminDashboard"; // Analytics Dashboard

function AdminPanel({ defaultTab = "analytics" }) {
  const [activeTab, setActiveTab] = useState(defaultTab);

  return (
    <div className="container mt-4">
      <h2>Admin Panel</h2>

      <div className="btn-group my-3" role="group">
        <button
          className={`btn ${activeTab === "analytics" ? "btn-primary" : "btn-outline-primary"}`}
          onClick={() => setActiveTab("analytics")}
        >
          Analytics
        </button>
        <button
          className={`btn ${activeTab === "accounts" ? "btn-primary" : "btn-outline-primary"}`}
          onClick={() => setActiveTab("accounts")}
        >
          Accounts
        </button>
      </div>

      <div>
        {activeTab === "analytics" && <AdminDashboard />}
        {activeTab === "accounts" && <Admin />}
      </div>
    </div>
  );
}

export default AdminPanel;
