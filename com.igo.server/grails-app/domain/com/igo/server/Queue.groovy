package com.igo.server

import java.util.Date

class Queue {
	Date startdate = new Date()
	Date enddate = new Date()
	Date signaldate
	boolean finished
	String type
	String description
	long idprocess
	int ord
	User user
	String status
	String initstatus
	Task task
	Queue parent
	Taskstatus taskstatus
	int repeatcount
	int maxrepeat
	
	static constraints = {
		type blank: false, unique: false
		startdate blank: false
	}
	
	static hasOne = [idprocess:Process]
}
