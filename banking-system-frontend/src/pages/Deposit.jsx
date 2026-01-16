import { useState } from "react";
import api from "../api/axiosConfig";

function Deposit() {
  const [accountId, setAccountId] = useState("");
  const [amount, setAmount] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/account/deposit", {
        accountId,
        amount
      });
      alert("Deposit successful");
    } catch {
      alert("Deposit failed");
    }
  };

  return (
    <div className="container mt-4">
      <h2>Deposit</h2>

      <form onSubmit={submit}>
        <input
          className="form-control my-2"
          placeholder="Account ID"
          value={accountId}
          onChange={e => setAccountId(e.target.value)}
        />

        <input
          className="form-control my-2"
          placeholder="Amount"
          type="number"
          value={amount}
          onChange={e => setAmount(e.target.value)}
        />

        <button className="btn btn-success">Deposit</button>
      </form>
    </div>
  );
}

export default Deposit;
