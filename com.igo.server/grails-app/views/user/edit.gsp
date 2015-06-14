<html>
<head>
<meta name="layout" content="main">
<title>Edit User</title>
</head>

<body id="edit">
	<h2>Edit User:</h2>

	<g:form action="edit">
		<input type="hidden" name="id" value="${user.id}">
		<table>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'user', bean:user, field:'login', label:'Логин']" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'user', bean:user, field:'username', label:'Имя']" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'user', bean:user, field:'password', label:'Пароль']" />
				</th>
			</tr>
			<tr>
				<th>Роль: <br /> <g:select name="roleSelect" from="${roles}"
						optionKey="name" optionValue="description"
						value="${ user.role ? user.role.description : '1'}" />
				</th>
			</tr>
			<tr>
				<td>
					<table>
						<tr>
							<td>
								<p class="submit">
									<input type="submit" value="Сохранить" />
								</p>
							</td>
							<td><g:link controller="user" action="list">
									<input type="button" value="Отмена" />
								</g:link></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</g:form>
</body>
</html>
