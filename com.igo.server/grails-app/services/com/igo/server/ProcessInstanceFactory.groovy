package com.igo.server

class ProcessInstanceFactory {
	def createInstance(long idprocess){
		Process item = Process.find("from Process as p where p.id = ? and p.active = 1", [idprocess])

		if(item != null){
			println "ProcessInstanceFactory.createInstance...idprocess=" + item.id

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
