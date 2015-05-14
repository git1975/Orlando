package com.igo.server

class User {

	String login
	String username
	String password
	
	Role role 
	
	static constraints = {
		username blank: false, unique: true
		login blank: false, unique: true
		password blank: false
		role blank: true
	}
}
