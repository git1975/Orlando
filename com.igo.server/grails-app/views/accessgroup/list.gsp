<html>
<head>
<meta name="layout" content="main">
<title>Линия коммуникации</title>
</head>
<body id="accessgroup">
	<g:form action='delete'>
		<h2>Линия коммуникации:</h2>

		<table>
			<thead>
				<tr>
					<th>Код</th>
					<th>Наименование</th>
					<th>Описание</th>
					<th></th>
				</tr>
			</thead>
			<g:each var="item" in="${items}">
				<tr>
					<td>
						${item.code}
					</td>
					<td>
						${item.name}
					</td>
					<td>
						${item.description}
					</td>
					<td><g:form method="GET" url="[action:'edit', id:item.id]">
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