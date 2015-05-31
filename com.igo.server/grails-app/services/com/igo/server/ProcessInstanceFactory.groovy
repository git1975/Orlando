package com.igo.server

class ProcessInstanceFactory {
	def createInstance(long idprocess){
		println "ProcessInstanceFactory.createInstance...idprocess=" + idprocess

		List process = Process.findAll("from Process as p where p.id = ?", [idprocess])
		for(Process item : process){
			Task[] tasks = item.tasks
			println "ProcessInstanceFactory.createInstance...tasks=" + tasks
			tasks.each{t ->
				Date startdt = t.startdate
				Date signaldt = t.signaldate
				Date enddt = t.enddate
				if(item.repeatevery > 0){
					Date now = new Date();
					startdt = Utils.addMinutes(now, t.startdate)
					signaldt = Utils.addMinutes(now, t.signaldate)
					enddt = Utils.addMinutes(now, t.enddate)
				}
				Queue msg = new Queue(type: 'Task', finished: false, description: t.description, idprocess: idprocess, ord: t.ord, user: t.getUser(), task: t, status: 'INIT',
				startdate: startdt, signaldate: signaldt, enddate: enddt)
				.save(failOnError: true)
			}
		}
	}
}
