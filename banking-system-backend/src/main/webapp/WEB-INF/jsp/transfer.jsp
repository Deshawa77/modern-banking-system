<html>
<head>
    <title>Transfer</title>
</head>
<body>

<h2>Transfer Funds</h2>

<form action="/jsp/transfer" method="post">
    From Account: <input type="text" name="fromAccount" required /><br><br>
    To Account: <input type="text" name="toAccount" required /><br><br>
    Amount: <input type="number" name="amount" required /><br><br>

    <button type="submit">Transfer</button>
</form>

<a href="/jsp/dashboard">Back</a>

</body>
</html>
