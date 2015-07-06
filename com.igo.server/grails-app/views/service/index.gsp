<html>
<head>
<script type="text/javascript">
function doReset(){
	if(confirm('Произвести сброс Базы Данных?')){
		${remoteFunction(controller: 'service', action: 'reset', onSuccess: 'doSuccessReset(data);')}
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
				<td><input type="button" onclick="doReset();" value="Сброс"/>
				</td>
			</tr>
		</table>
		
	</g:form>
</body>
</html>