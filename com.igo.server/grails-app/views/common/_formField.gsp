${label}: 
	<g:fieldError bean="${bean}" field="${field}" />

<br/>
<g:textField name="${name + '_' + field}"
             value="${fieldValue(bean:bean, field:field)}" required="${required}" />