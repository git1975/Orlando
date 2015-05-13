package com.igo.server

class Process {
	String name
	String description
	boolean autostart
	boolean repeat
	Date startdate

	static hasMany = [tasks: Task]

	static constraints = {
		name blank: false, unique: true
	}

	static mapping = {
		autostart defaultValue: "true"
		repeat defaultValue: "true"
	}
}
