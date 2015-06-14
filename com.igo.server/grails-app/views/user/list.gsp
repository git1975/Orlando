<html>
<head>
<meta name="layout" content="main">
<title>Users</title>
</head>
<body id="users">
<g:form action='delete'>
	<h2>Users:</h2>

	<table>
		<thead>
			<tr>
				<th></th>
				<th>Логин</th>
				<th>Имя</th>
				<th>Роль</th>
				<th></th>
			</tr>
		</thead>
		<g:each var="user" in="${users}">
			<tr>
				<td><g:checkBox name="users.${user.id}" value="${false}"/> </td>
				<td>
					${user.login}
				</td>
				<td>
					${user.username}
				</td>
				<td>
					${ user.role ? user.role.description : '?'}
				</td>
				<td>
					<g:form method="GET" url="[action:'edit', id:user.id]">
						<p class="submit"><input type="submit" value="Редактировать"/></p>
					</g:form>
				</td>
			</tr>
		</g:each>
	</table>
	<g:link controller="user" action="add"><input type="button" value="Добавить"/></g:link>
	<g:actionSubmit action="delete" value="Удалить" onclick="return confirm('Удалить?')"/>
</g:form>
</body>
</html>