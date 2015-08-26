<html>
<head>
<meta name="layout" content="main">
<title>Админка</title>
</head>
<body id="edit">
	<g:form>
		<h2>Админка:</h2>
		<table>
			<tr>
				<td><g:link controller="accessgroup" action="list">Группы доступа</g:link>
				</td>
			</tr>
			<tr>
				<td><g:link controller="user" action="list">Пользователи</g:link>
				</td>
			</tr>
			<tr>
				<td><g:link controller="button" action="list">Кнопки</g:link>
				</td>
			</tr>
			<tr>
				<td><g:link controller="register" action="list">Регистры</g:link>
				</td>
			</tr>
			<tr>
				<td><g:link controller="taskstatus" action="list">Статусы задач</g:link>
				</td>
			</tr>
			<tr>
				<td><g:link controller="process" action="list">Циклы</g:link>
				</td>
			</tr>
			<tr>
				<td><g:link controller="task" action="list">Задачи</g:link>
				</td>
			</tr>
			<tr>
				<td><g:link controller="service" action="index">Сервис</g:link>
				</td>
			</tr>
		</table>
	</g:form>
</body>
</html>