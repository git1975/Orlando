<html>
<head>
<meta name="layout" content="main">
<title>Add User</title>
</head>

<body id="add">
	<h2>
		<g:if test="${!user.id}">New </g:if>
		User:
	</h2>

	<g:form action="${ user.id ? 'edit' : 'add'}" id="${user?.id}">
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
						optionKey="name" optionValue="description" />
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
