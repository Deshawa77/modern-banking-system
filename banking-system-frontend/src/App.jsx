import { Routes, Route, Navigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "./context/AuthContext";

import ProtectedRoute from "./components/ProtectedRoute";
import AdminRoute from "./pages/AdminRoute";
import Navbar from "./components/Navbar";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Deposit from "./pages/Deposit";
import Withdraw from "./pages/Withdraw";
import Balance from "./pages/Balance";
import Transactions from "./pages/Transactions";

import AdminPanel from "./pages/AdminPanel";

function App() {
  const { token } = useContext(AuthContext);

  return (
    <>
      {token && <Navbar />}

      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route
          path="/login"
          element={token ? <Navigate to="/dashboard" /> : <Login />}
        />
        <Route path="/register" element={<Register />} />

        {/* User Protected Routes */}
        <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
        <Route path="/deposit" element={<ProtectedRoute><Deposit /></ProtectedRoute>} />
        <Route path="/withdraw" element={<ProtectedRoute><Withdraw /></ProtectedRoute>} />
        <Route path="/balance" element={<ProtectedRoute><Balance /></ProtectedRoute>} />
        <Route path="/transactions" element={<ProtectedRoute><Transactions /></ProtectedRoute>} />

        {/* Admin Route — Single Route */}
        <Route
          path="/admin"
          element={
            <AdminRoute>
              <AdminPanel /> {/* Handles both Analytics & Account tabs internally */}
            </AdminRoute>
          }
        />

        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </>
  );
}

export default App;
