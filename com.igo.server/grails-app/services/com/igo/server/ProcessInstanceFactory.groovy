package com.igo.server

class ProcessInstanceFactory {
	static def createInstance(long idprocess){
		print "ProcessInstanceFactory.createInstance...idprocess=" + idprocess
		
		List process = Process.findAll("from Process as p where p.id = ?", [idprocess])
		for(Process item : process){
		//process.each{item -> 
			Task[] tasks = item.tasks
			print tasks.toString()
			tasks.each{t -> 
				Queue msg = new Queue(type: 'Task', finished: false, description: t.description, idprocess: idprocess, ord: t.ord, user: t.getUser())
				.save(failOnError: true)
			}
		}
	}
}
