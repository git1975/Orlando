<html>
<head>
<meta name="layout" content="main">
<title>Добавить Линию коммуникации</title>
</head>

<body id="add">
	<h2>
		Добавить Линию коммуникации:
	</h2>

	<g:form action="add" id="${item.id}">
		<table>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'code', label:'Код', required: true]" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'name', label:'Наименование', required: true]" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'description', label:'Описание']" />
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
							<td><g:link controller="accessgroup" action="list">
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
