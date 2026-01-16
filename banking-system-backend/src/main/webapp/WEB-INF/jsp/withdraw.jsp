<html>
<head>
    <title>Withdraw</title>
</head>
<body>

<h2>Withdraw Funds</h2>

<form action="/jsp/withdraw" method="post">
    Account Number:
    <input type="text" name="accountNumber" required /><br><br>

    Amount:
    <input type="number" name="amount" min="1" required /><br><br>

    <button type="submit">Withdraw</button>
</form>

<a href="/jsp/dashboard">Back</a>

</body>
</html>
