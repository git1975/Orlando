package com.igo.server

import grails.transaction.Transactional

@Transactional
class DataService {

	def createUser(String username, String password){
		def user = new User(username: username, password: password)
		user.save(failOnError: true)
		user
	}
	
	def deleteUser(long id){
		def user = User.find("from User where id=?", [id])
		if(user != null){
			user.delete(flush: true)
		}
	}
}
