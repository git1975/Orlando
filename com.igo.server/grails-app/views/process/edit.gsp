<html>
<head>
<meta name="layout" content="main">
<title>Цикл</title>
</head>

<body id="edit">
	<h2>Цикл:</h2>

	<g:form action="edit">
		<input type="hidden" name="id" value="${item.id}">
		<table>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'name', label:'Код']" /></th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'description', label:'Наименование']" />
				</th>
			</tr>
			<tr>
				<th>Активный: <br /> <g:select name="active"
						from="${['true': 'Да', 'false': 'Нет']}" optionKey="key"
						optionValue="value" value="${ item.active }" />
				</th>
			</tr>
			<tr>
				<th>Автостарт: <br /> <g:select name="autostart"
						from="${['true': 'Да', 'false': 'Нет']}" optionKey="key"
						optionValue="value" value="${ item.autostart }" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'repeatevery', label:'Повторять (мин)']" />
				</th>
			</tr>
			<tr>
				<th>Дата начала: <br /> 
					<g:textField name="item_startdate" value="${formatDate(format:'yyyy-dd-MM HH:mm:ss', date:item.startdate)}"/>
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
							<td><g:link controller="process" action="list">
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
