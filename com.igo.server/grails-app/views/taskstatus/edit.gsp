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
			<g:each status="idx" var="button" in="${buttons}">
				<tr>
					<td><g:if test="${button.id}">
							${button.code}
						</g:if> <g:else>
							<g:select name="allbuttons.${idx}" from="${allbuttons}"
								optionKey="code" optionValue="name" />
						</g:else></td>
					<td>
						${button.name}
					</td>
					<td>
						${button.replystatus}
					</td>
					<td><g:form method="GET" url="[action:'delButton']">
							<p class="submit">
								<g:link action="delButton" id="${button.id}" alt="Удалить"
									onclick="return confirm('Удалить?')"
									params="[id:button.id, mainid:item.id]">
									<g:img file="delete.png" />
								</g:link>
							</p>
						</g:form></td>
				</tr>
			</g:each>
		</table>
		<table>
			<tr>
				<td><g:form method="GET" url="[action:'addButton']">
						<p class="submit">
							<g:link action="addButton" params="[mainid:item.id]">
								<g:img file="button-add.png" />
							</g:link>
						</p>
					</g:form></td>
			</tr>
		</table>
		<table>
			<tr>
				<td>
					<p class="submit">
						<input type="submit" value="Сохранить" />
					</p>
				</td>
				<td><g:link controller="taskstatus" action="cancel">
						<input type="button" value="Отмена" />
					</g:link></td>
			</tr>
		</table>
	</g:form>
</body>
</html>
