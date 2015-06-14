<html>
<head>
<meta name="layout" content="main">
<title>Новый Статус задачи</title>
</head>

<body id="add">
	<h2>
		Новый Статус задачи:
	</h2>

	<g:form action="add" id="${item.id}">
		<table>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'msgtext', label:'Текст']" />
				</th>
			</tr>
			<tr>
				<th>Тип: <br /> <g:select name="msgtype" from="${['INFO': 'INFO', 'CMD': 'CMD']}"
						optionKey="key" optionValue="value" value="${ item.msgtype }"/>
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'status', label:'Статус']" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'lifetime', label:'Время жизни']" />
				</th>
			</tr>
			<tr>
				<th>Светофор: <br /> <g:select name="color" from="${['1': 'зеленый', '2': 'желтый', '3': 'красный']}"
						optionKey="key" optionValue="value" value="${ item.color }"/>
				</th>
			</tr>
			<tr>
				<th>Задача: <br /> <g:select name="taskSelect" from="${tasks}"
						optionKey="name" optionValue="description" value="start"/>
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
							<td><g:link controller="taskStatus" action="list">
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
