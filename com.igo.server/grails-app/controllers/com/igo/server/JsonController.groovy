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
		
		List list = Queue.findAll("from Queue as q where q.finished = ? and q.type = 'Task'", [false])
		
		render list as JSON;
	}
}
