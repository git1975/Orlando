<html>
<head>
<meta name="layout" content="main">
<title>Статус задачи</title>
</head>

<body id="edit">
	<h2>Статус задачи:</h2>

	<g:form action="edit">
		<input type="hidden" name="id" value="${item.id}">
		<table>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'msgtext', label:'Текст']" />
				</th>
			</tr>
			<tr>
				<th>Тип: <br /> <g:select name="msgtype"
						from="${['INFO': 'INFO', 'CMD': 'CMD']}" optionKey="key"
						optionValue="value" value="${ item.msgtype }" />
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
				<th>Светофор: <br /> <g:select name="color"
						from="${['1': 'зеленый', '2': 'желтый', '3': 'красный']}"
						optionKey="key" optionValue="value" value="${ item.color }" />
				</th>
			</tr>
			<tr>
				<th>Задача: <br /> <g:select name="taskSelect" from="${tasks}"
						optionKey="name" optionValue="description"
						value="${ item.task ? item.task.name : '1'}" />
				</th>
			</tr>
		</table>
		<table>
			<g:each var="buttons" in="${item.buttons}">
				<tr>
					<td>
						${buttons.code}
					</td>
					<td>
						${buttons.name}
					</td>
					<td>
						${buttons.replystatus}
					</td>
				</tr>
			</g:each>
		</table>
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
	</g:form>
</body>
</html>
