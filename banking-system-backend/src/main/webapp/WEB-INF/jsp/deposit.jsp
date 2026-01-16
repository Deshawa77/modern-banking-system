<html>
<head>
    <title>Deposit</title>
</head>
<body>

<h2>Deposit Funds</h2>

<form action="/jsp/deposit" method="post">
    Account Number:
    <input type="text" name="accountNumber" required /><br><br>

    Amount:
    <input type="number" name="amount" min="1" required /><br><br>

    <button type="submit">Deposit</button>
</form>

<a href="/jsp/dashboard">Back</a>

</body>
</html>
