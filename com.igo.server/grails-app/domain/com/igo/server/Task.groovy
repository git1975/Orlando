package com.igo.server

class Task {
	String name
	String description
	
	static hasMany = [deviations: Deviation]
	
    static constraints = {
		name blank: false, unique: true
    }
}
