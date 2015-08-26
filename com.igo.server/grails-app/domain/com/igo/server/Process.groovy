package com.igo.server

class Process {
	String name
	String description
	boolean autostart
	boolean active = true
	long repeatevery = 0
	Date startdate = new Date()
	Accessgroup accessgroup

	static constraints = {
		name blank: false, unique: true
		accessgroup blank: false
	}

	static mapping = {
		autostart defaultValue: "true"
		repeatevery defaultValue: 0L
	}
}
