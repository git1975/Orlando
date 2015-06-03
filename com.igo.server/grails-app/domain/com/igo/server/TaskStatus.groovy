package com.igo.server

class TaskStatus {
	Task task
	String status
	String msgtype
	String sendTo
	String msgtext
	Role role
	int color = 1
	int lifetime = 1
	
	static hasMany = [buttons: Button]

    static constraints = {
    }
}
