package com.igo.server

class TaskStatus {
	Task task
	String status
	String msgtype
	String sendTo
	String msgtext
	
	static hasMany = [buttons: Button]

    static constraints = {
    }
}
