package com.igo.server

class Accessgroup {
	String code
	String name
	String description
	
    static constraints = {
		code nullable: false, unique: true
		name nullable: false
    }
}
