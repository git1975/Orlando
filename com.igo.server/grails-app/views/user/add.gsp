<html>
<head>
<meta name="layout" content="main">
<title>Новый пользователь</title>
</head>

<body id="add">
	<h2>
		Новый пользователь:
	</h2>

	<g:form action="add" id="${item?.id}">
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
				<th>Роль: <br /> <g:select name="roleSelect" from="${roles}"
						optionKey="code" optionValue="name" />
				</th>
			</tr>
			<tr>
				<th>Линия коммуникации: <br /> <g:select name="accessgroupSelect" from="${accessgroup}" noSelection="${['null':'Нет...']}"
						optionKey="code" optionValue="name" value="${ item.accessgroup ? item.accessgroup.name : ''}"/>
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
