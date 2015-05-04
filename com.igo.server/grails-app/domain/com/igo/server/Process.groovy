package com.igo.server

class Process {
	String name
	String description
	
	static hasMany = [tasks: Task]
	
    static constraints = {
		name blank: false, unique: true
    }
}
