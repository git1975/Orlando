package com.igo.server

class Deviation {
	String name
	
    static constraints = {
		name blank: false, unique: true
    }
}
