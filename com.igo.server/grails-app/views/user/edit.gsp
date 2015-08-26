<html>
<head>
<meta name="layout" content="main">
<title>Пользователь</title>
</head>

<body id="edit">
	<h2>Пользователь:</h2>

	<g:form action="edit">
		<input type="hidden" name="id" value="${item.id}">
		<table>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'login', label:'Логин', required: true]" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'username', label:'Имя', required: true]" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'password', label:'Пароль']" />
				</th>
			</tr>
			<tr>
				<th>Роль: <br /> <g:select name="roleSelect" from="${roles}" noSelection="${['null':'Нет...']}"
						optionKey="code" optionValue="name"
						value="${ item.role ? item.role.code : ''}" />
				</th>
			</tr>
			<tr>
				<th>Группа доступа: <br /> <g:select name="accessgroupSelect" from="${accessgroup}" noSelection="${['null':'Нет...']}"
						optionKey="code" optionValue="name" value="${ item.accessgroup ? item.accessgroup.code : ''}"/>
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
