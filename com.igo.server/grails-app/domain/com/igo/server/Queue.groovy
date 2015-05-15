package com.igo.server

import java.util.Date

class Queue {
	Date startdate = new Date()
	Date enddate = new Date()
	boolean finished
	String type
	String description
	long idprocess
	int ord
	User user
	
	static constraints = {
		type blank: false, unique: false
		startdate blank: false
	}
	
	static hasOne = [idprocess:Process]
}
