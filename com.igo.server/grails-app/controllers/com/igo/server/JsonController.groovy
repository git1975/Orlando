package com.igo.server

import grails.converters.JSON

class JsonController {
//	static allowedMethods = [list:'GET',
//		show:'GET',
//		edit:['GET', 'POST'],
//		save:'POST',
//		update:['POST','PUT'],
//		delete:['POST','DELETE']
//	]
	
    def show() { 
		print "JsonController.show"
		
		List list = Queue.findAll("from Queue as q where q.finished = ? and q.type = 'Task' order by ord", [false])
		
		render list as JSON;
	}
	
	def login() {
		print "JsonController.login." + params.login
		
		com.igo.server.User item = com.igo.server.User.find("from User as a where a.login = ? and password = ?", [params.login, params.password])
		
		render item as JSON;
	}
}
