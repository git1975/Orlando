package com.igo.server

class User {

	String username
	String password
	
	static constraints = {
		username blank: false, unique: true
		password blank: false
	}
}
