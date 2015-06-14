<html>
<head>
<meta name="layout" content="main">
<title>Пользователи</title>
</head>
<body id="users">
<g:form action='delete'>
	<h2>Пользователи:</h2>

	<table>
		<thead>
			<tr>
				<th>Логин</th>
				<th>Имя</th>
				<th>Роль</th>
				<th></th>
			</tr>
		</thead>
		<g:each var="item" in="${items}">
			<tr>
				<td>
					${item.login}
				</td>
				<td>
					${item.username}
				</td>
				<td>
					${ item.role ? item.role.description : '?'}
				</td>
				<td><g:form method="GET" url="[action:'edit', id:item.id]">
							<p class="submit">
								<!--input type="submit" value="Редактировать"/-->
								<g:link action="edit" id="${item.id}" alt="Редактировать">
									<g:img file="edit.png" />
								</g:link>
								<g:link action="delete" id="${item.id}" alt="Удалить" onclick="return confirm('Удалить?')">
									<g:img file="delete.png" />
								</g:link>
							</p>
						</g:form></td>
			</tr>
		</g:each>
	</table>
	<g:link controller="user" action="add"><input type="button" value="Добавить"/></g:link>
</g:form>
</body>
</html>