package com.igo.server

class Register {
	String code
	String name
	String description

	static constraints = {
		code blank: false, unique: true
		name blank: false
	}
}
