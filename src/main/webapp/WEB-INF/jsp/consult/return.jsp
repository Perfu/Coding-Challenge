<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>User information</title>
</head>
<body>

	<div>
		<h2>User Information (private section)</h2>

		<table border="1">
			<thead>
				<tr>
					<th>Account name</th>
					<th>Email</th>
					<th>FirstName</th>
					<th>LastName</th>
					<th>openId</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>${user.account.name}</td>
					<td>${user.email}</td>
					<td>${user.firstName}</td>
					<td>${user.lastName}</td>
					<td>${user.openId}</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>