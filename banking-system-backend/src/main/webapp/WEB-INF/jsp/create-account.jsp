<html>
<head>
    <title>Create Account</title>
</head>
<body>

<h2>Create Account</h2>

<form action="/jsp/create-account" method="post">
    Account Type:
    <select name="accountType">
        <option>SAVINGS</option>
        <option>CURRENT</option>
    </select>
    <br><br>

    Initial Balance:
    <input type="number" name="balance" required />
    <br><br>

    <button type="submit">Create</button>
</form>

<a href="/jsp/dashboard">Back</a>

</body>
</html>
