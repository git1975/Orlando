package com.igo.server

class TaskStatus {
	Task task
	String status
	String msgtype
	String sendTo
	String msgtext
	Role role
	
	static hasMany = [buttons: Button]

    static constraints = {
    }
}
