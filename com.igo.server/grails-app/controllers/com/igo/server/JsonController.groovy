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
		ArrayList<Task> tasks = new ArrayList<Task>(2);
		Task task = new Task();
		task.id = 1;
		task.name = "task1";
		Task task2 = new Task();
		task2.id = 2;
		task2.name = "task2";
		tasks.add(task);
		tasks.add(task2);
		
		List list = Queue.findAll("from Queue as q where q.finished = ? and q.type = 'Task'", [false])
		
		render list as JSON;
	}
}
