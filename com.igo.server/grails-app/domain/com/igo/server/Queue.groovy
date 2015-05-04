package com.igo.server

import java.sql.Timestamp;

class Queue {
	Date startdate = new Date()
	Date enddate
	boolean comleted
	String type
	String description
	
	static constraints = {
		type blank: false, unique: false
		startdate blank: false
	}
}
