package com.igo.server

class ProcessInstanceFactory {
	static def createInstance(long idprocess){
		print "createInstance..."
		
		List process = Process.findAll("from Process as p where p.id = ?", [idprocess])
		//for(Process item : process){
		process.each{item -> 
			Task[] tasks = item.getTasks()
			print tasks.toString()
			tasks.each{t -> 
				Queue msg = new Queue(type: 'Task', finished: false, description: t.getDescription(), idprocess: idprocess)
				.save(failOnError: true)
			}
		}
	}
}
