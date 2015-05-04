package com.igo.server

class Task {
	String id
	String name
	
    static constraints = {
		id blank: false, unique: true
    }
}
