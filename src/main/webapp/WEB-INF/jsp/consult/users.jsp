<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Users of this webApp</title>
</head>
<body>

	<div>
	<table border="1">
			<tr>
				<th>uuid</th>
				<th>Name</th>
			</tr>
			<c:forEach items="${accounts}" var="account">
				<tr>
					<td>${account.uuid}</td>
					<td>${account.name}</td>
				</tr>
			</c:forEach>
		</table>
	
	
		<table border="1">
			<tr>
				<th>Account</th>
				<th>First Name</th>
				<th>Last Name</th>
				<th>Email</th>
			</tr>
			<c:forEach items="${users}" var="user">
				<tr>
					<td>${user.account}</td>
					<td>${user.firstName}</td>
					<td>${user.lastName}</td>
					<td>${user.email}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>