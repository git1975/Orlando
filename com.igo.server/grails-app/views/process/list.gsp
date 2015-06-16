<html>
<head>
<meta name="layout" content="main">
<title>Циклы</title>
</head>
<body id="process">
	<g:form>
		<h2>Циклы:</h2>

		<table>
			<thead>
				<tr>
					<th>Код</th>
					<th>Наименование</th>
					<th>Активный</th>
					<th></th>
				</tr>
			</thead>
			<g:each var="item" in="${items}">
				<tr>
					<td>
						${item.name}
					</td>
					<td>
						${item.description}
					</td>
					<td>
						${ item.active}
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