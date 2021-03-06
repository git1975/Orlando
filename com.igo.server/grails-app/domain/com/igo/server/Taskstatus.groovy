package com.igo.server

class Taskstatus {
	Task task
	String status
	String msgtype
	String sendTo
	String msgtext
	Role role
	int color = 1
	int lifetime = 1
	int maxrepeat = 0
	int repeatevery = 1
	List buttons = new ArrayList()
	String registers	
	
	static hasMany = [buttons: Button]
	
	/*static mapping = {
		buttons cascade:"all,delete-orphan"
		}*/
	
	static constraints = {
		registers size: 1..1000
	}

	def getButtonsList() {
		return LazyList.decorate(
			  buttons,
			  FactoryUtils.instantiateFactory(Button.class))
	}
}
