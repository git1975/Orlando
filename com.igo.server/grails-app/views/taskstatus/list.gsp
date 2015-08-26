<html>
<head>
<meta name="layout" content="main">
<title>Статусы задач</title>
</head>
<body id="buttons">
	<g:form action='delete'>
		<h2>Статусы задач:</h2>

		<table>
			<thead>
				<tr>
					<th>Текст</th>
					<th>Тип</th>
					<th>Статус</th>
					<th>Время жизни</th>
					<th>Светофор</th>
					<th>Кому</th>
					<th>Задача</th>
					<th></th>
				</tr>
			</thead>
			<g:each var="item" in="${items}">
				<tr>
					<td>
						${item.msgtext}
					</td>
					<td>
						${item.msgtype}
					</td>
					<td>
						${item.status}
					</td>
					<td>
						${item.lifetime}
					</td>
					<td>
						${item.color}
					</td>
					<td>
						${item.sendTo}
					</td>
					<td>
					${ item.task ? item.task.process.description + '.' + item.task.description: '?'}
					</td>
					<td>
						<g:form method="GET" url="[action:'edit', id:item.id]">
							<p class="submit">
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