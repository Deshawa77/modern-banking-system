<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Transactions</title>
</head>
<body>

<h2>Transactions</h2>

<table border="1">
    <tr>
        <th>Type</th>
        <th>Amount</th>
        <th>Date</th>
    </tr>

    <c:forEach var="tx" items="${transactions}">
        <tr>
            <td>${tx.transactionType}</td>
            <td>${tx.amount}</td>
            <td>${tx.timestamp}</td>
        </tr>
    </c:forEach>
</table>

<br>
<a href="/jsp/dashboard">Back</a>

</body>
</html>
