import { Link, useNavigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

function Navbar() {
  const { logout, role } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light px-3">
      <Link className="navbar-brand" to="/dashboard">
        BankingApp
      </Link>

      <div className="collapse navbar-collapse">
        <ul className="navbar-nav me-auto">
          {/* User Links */}
          <li className="nav-item">
            <Link className="nav-link" to="/dashboard">Dashboard</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/deposit">Deposit</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/withdraw">Withdraw</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/balance">Balance</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/transactions">Transactions</Link>
          </li>

          {role?.toUpperCase() === "ADMIN" && (
            <li className="nav-item">
              <Link className="nav-link" to="/admin">Admin</Link>
            </li>
          )}


        </ul>

        <button className="btn btn-danger" onClick={handleLogout}>
          Logout
        </button>
      </div>
    </nav>
  );
}

export default Navbar;
