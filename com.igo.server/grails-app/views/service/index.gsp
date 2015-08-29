<html>
<head>
<script type="text/javascript">
function doReset(){
	if(confirm('Произвести сброс к длинному циклу?')){
		${remoteFunction(controller: 'service', action: 'reset', onSuccess: 'doSuccessReset(data);')}
	}
	return
}
function doResetDemo(){
	if(confirm('Произвести сброс к Демо?')){
		${remoteFunction(controller: 'service', action: 'resetDemo', onSuccess: 'doSuccessReset(data);')}
	}
	return
}
function doResetChat(){
	if(confirm('Стереть чат и очередь?')){
		${remoteFunction(controller: 'service', action: 'resetChat', onSuccess: 'doSuccessReset(data);')}
	}
	return
}
function doSuccessReset(data){
	alert(data);
}
</script>
<meta name="layout" content="main">
<title>Админка</title>
</head>

<body id="edit">
	<g:form>
		<h2>Админка:</h2>
		<table>
			<tr>
				<td><input type="button" onclick="doResetDemo();" value="Сброс к демо"/><br/>
				</td>
			</tr>
			<tr>
				<td><input type="button" onclick="doResetChat();" value="Стереть чат и очередь"/><br/>
				</td>
			</tr>
			<tr>
				<td><a href="/com.igo.server/com.igo.ui.andro.apk">Скачать приложение Android</a>
				</td>
			</tr>
		</table>
		
	</g:form>
</body>
</html>