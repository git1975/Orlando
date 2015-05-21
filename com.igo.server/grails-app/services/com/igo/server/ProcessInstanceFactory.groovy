package com.igo.server

class ProcessInstanceFactory {
	def createInstance(long idprocess){
		print "ProcessInstanceFactory.createInstance...idprocess=" + idprocess

		List process = Process.findAll("from Process as p where p.id = ?", [idprocess])
		for(Process item : process){
			Task[] tasks = item.tasks
			print "ProcessInstanceFactory.createInstance...tasks=" + tasks
			tasks.each{t ->
				Queue msg = new Queue(type: 'Task', finished: false, description: t.description, idprocess: idprocess, ord: t.ord, user: t.getUser(), task: t, status: 'INIT',
				startdate: t.startdate, enddate: t.enddate, signaldate: t.signaldate)
				.save(failOnError: true)
			}
		}
	}
}
