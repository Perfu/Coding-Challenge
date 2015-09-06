<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Users of this webApp</title>
</head>
<body>

	<div>
		<h2>Users of the application</h2>

		<c:choose>
			<c:when test="${not empty accounts}">
				<ul>
					<c:forEach items="${accounts}" var="account">
						<li><b>Company : </b>${account.name}, <b>Edition : </b>${account.edition}

							<table border="1">
								<tr>
									<th>First Name</th>
								</tr>
								<c:forEach items="${account.users}" var="user">
									<tr>
										<td>${user.firstName}</td>
									</tr>
								</c:forEach>
							</table></li>
					</c:forEach>
				</ul>
			</c:when>
			<c:otherwise>
				No user in the application.
			</c:otherwise>
		</c:choose>

	</div>
</body>
</html>