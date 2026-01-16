<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Accounts</title>
</head>
<body>

<h2>My Accounts</h2>

<table border="1">
    <tr>
        <th>Account Number</th>
        <th>Type</th>
        <th>Balance</th>
    </tr>

    <c:forEach var="acc" items="${accounts}">
        <tr>
            <td>${acc.accountNumber}</td>
            <td>${acc.accountType}</td>
            <td>${acc.balance}</td>
        </tr>
    </c:forEach>
</table>

<br>
<a href="/jsp/dashboard">Back</a>

</body>
</html>
