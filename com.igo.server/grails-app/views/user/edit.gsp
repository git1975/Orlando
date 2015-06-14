<html>
	<head>
		<meta name="layout" content="main">
		<title>Add User</title>
	</head>

	<body id="edit">
		<h2><g:if test="${!user.id}">New </g:if>User:</h2>

		<g:form action="${ user.id ? 'edit' : 'add'}" id="${user?.id}">
			<table>
				<tr>
					<th>
						<g:render template="/common/formField"
						          model="[name:'user', bean:user, field:'username', label:'User Name']" />
					</th>
				</tr>
				<tr>
					<th>
						<g:render template="/common/formField"
						          model="[name:'user', bean:user, field:'password', label:'Password']" />
					</th>
				</tr>
				<tr>
					<td>
						<p class="submit"><input type="submit" value="${ user.id ? 'Update' : 'Add'} User"/></p>
					</td>
				</tr>
			</table>
		</g:form>
	</body>