package com.igo.server

class Task {
	String name
	String description
	
    static constraints = {
		name blank: false, unique: true
    }
}
