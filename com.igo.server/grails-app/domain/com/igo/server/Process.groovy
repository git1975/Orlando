package com.igo.server

class Process {
	String name
	String description
	boolean autostart
	boolean active = true
	long repeatevery = 0
	Date startdate = new Date()

	static hasMany = [tasks: Task]

	static constraints = {
		name blank: false, unique: true
	}

	static mapping = {
		autostart defaultValue: "true"
		repeatevery defaultValue: 0L
	}
}
