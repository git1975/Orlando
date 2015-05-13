package com.igo.server

import java.util.Date;

class Task {
	String name
	String description
	boolean autostart
	Date startdate
	Date enddate
	
	static hasMany = [deviations: Deviation]
	
    static constraints = {
		name blank: false, unique: true
    }
	
	static mapping = {
		autostart defaultValue: "true"
	}
}
