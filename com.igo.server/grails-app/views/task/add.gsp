<html>
<head>
<meta name="layout" content="main">
<title>Новая Задача</title>
</head>

<body id="add">
	<h2>
		Новая Задача:
	</h2>

	<g:form action="add" id="${item?.id}">
		<table>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'name', label:'Код']" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'description', label:'Наименование']" />
				</th>
			</tr>
			<tr>
				<th><g:render template="/common/formField"
						model="[name:'item', bean:item, field:'ord', label:'Порядок']" />
				</th>
			</tr>
			<tr>
				<th>Процесс: <br /> <g:select name="processSelect" from="${process}"
						optionKey="name" optionValue="description" />
				</th>
			</tr>
			<tr>
				<th>Старт: <br /> 
					<g:textField name="item_startdate" value="${formatDate(format:'yyyy-dd-MM HH:mm:ss', date:item.startdate)}"/>
				</th>
			</tr>
			<tr>
				<th>Сигнал: <br /> 
					<g:textField name="item_signaldate" value="${formatDate(format:'yyyy-dd-MM HH:mm:ss', date:item.signaldate)}"/>
				</th>
			</tr>
			<tr>
				<th>Завершение: <br /> 
					<g:textField name="item_enddate" value="${formatDate(format:'yyyy-dd-MM HH:mm:ss', date:item.enddate)}"/>
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
							<td><g:link controller="task" action="list">
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
