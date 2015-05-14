package com.igo.server

import java.util.Date;

class Task {
	String name
	String description
	boolean autostart
	Date startdate = new Date()
	Date enddate = new Date()
	User user
	
	static hasMany = [deviations: Deviation]
	
	
    static constraints = {
		name blank: false, unique: true
		user blank: true
    }
	
	static mapping = {
		autostart defaultValue: "true"
	}
}
