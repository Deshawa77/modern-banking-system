import { useState } from "react";
import api from "../api/axiosConfig";

function Withdraw() {
  const [accountId, setAccountId] = useState("");
  const [amount, setAmount] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/account/withdraw", {
        accountId,
        amount
      });
      alert("Withdraw successful");
    } catch {
      alert("Insufficient balance or error");
    }
  };

  return (
    <div className="container mt-4">
      <h2>Withdraw</h2>

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

        <button className="btn btn-danger">Withdraw</button>
      </form>
    </div>
  );
}

export default Withdraw;
