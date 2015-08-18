<html>
<head>
<meta name="layout" content="main">
<title>Кнопка</title>
</head>

<body id="edit">
	<h2>Кнопка:</h2>

	<g:form action="edit">
		<input type="hidden" name="id" value="${item.id}">
		<table>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'code', label:'Код']" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'name', label:'Наименование']" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'replystatus', label:'Код ответа']" />
				</th>
			</tr>
			<tr>
				<th>Регистр: <br /> <g:select name="registerSelect" from="${register}" noSelection="${['null':'Нет...']}"
						optionKey="code" optionValue="name"
						value="${ item.register ? item.register.code : '1'}" />
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
							<td><g:link controller="button" action="list">
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
