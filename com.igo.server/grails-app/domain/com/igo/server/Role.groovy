package com.igo.server

class Role {
	String name
	String description
	
    static constraints = {
		name blank: false, unique: true
    }
}
