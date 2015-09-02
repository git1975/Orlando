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
					<th>Линия коммуникации</th>
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
						${ item.role ? item.role.name : '?'}
					</td>
					<td>
						${ item.accessgroup ? item.accessgroup.name : '?'}
					</td>
					<td><g:form method="GET" url="[action:'edit', id:item.id]">
							<p class="submit">
								<!--input type="submit" value="Редактировать"/-->
								<g:link action="edit" id="${item.id}" alt="Редактировать">
									<g:img file="edit.png" />
								</g:link>
								<g:link action="delete" id="${item.id}" alt="Удалить"
									onclick="return confirm('Удалить?')">
									<g:img file="delete.png" />
								</g:link>
							</p>
						</g:form></td>
				</tr>
			</g:each>
		</table>
		<table>
			<tr>
				<td><g:form method="GET" url="[action:'add']">
						<p class="submit">
							<g:link action="add">
								<g:img file="button-add.png" />
							</g:link>
						</p>
					</g:form></td>
			</tr>
		</table>
	</g:form>
</body>
</html>