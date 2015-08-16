package com.igo.server

class ProcessInstanceFactory {
	/**
	 * Создает экземпляры тасков в таблице Queue. Начальный статус всегда 'INIT'
	 * @param idprocess
	 * @return
	 */
	def createInstance(long idprocess){
		Process item = Process.find("from Process as p where p.id = ? and p.active = 1", [idprocess])

		if(item != null){
			log.debug("ProcessInstanceFactory.createInstance...idprocess=" + item.id)

			List<Task> tasks = Task.findAll("from Task where process=?", [item])

			log.debug("ProcessInstanceFactory.createInstance...tasks=" + tasks)
			for(Task t: tasks){
				Date startdt = t.startdate
				Date signaldt = t.signaldate
				Date enddt = t.enddate
				//if(item.repeatevery > 0){
				Date now = new Date();
				startdt = Utils.addMinutes(now, t.startdate)
				signaldt = Utils.addMinutes(now, t.signaldate)
				enddt = Utils.addMinutes(now, t.enddate)
				//}
				Queue msg = new Queue(type: 'Task', finished: false, description: t.description, idprocess: idprocess, ord: t.ord, user: t.getUser(), task: t, status: 'INIT',
				startdate: startdt, signaldate: signaldt, enddate: enddt)
				.save(failOnError: true)
			}
		}
	}

	def static createTaskstatusInstance(Queue parent, Taskstatus ts){
		Date startdt = ts.task.startdate
		Date signaldt = ts.task.signaldate
		Date enddt = ts.task.enddate
		Date now = new Date();
		startdt = Utils.addMinutes(now, ts.task.startdate)
		signaldt = Utils.addMinutes(now, ts.task.signaldate)
		enddt = Utils.addMinutes(now, ts.task.enddate)
		User user = User.findByLogin(ts.sendTo)

		Queue msg = new Queue(type: 'Taskstatus', finished: false, description: ts.task.description + ' <' + ts.msgtype + '> ' + ts.msgtext, 
			idprocess: ts.task.process.id, ord: 0, user: user, task: ts.task, status: ts.status,
			startdate: new Date(), signaldate: new Date(), enddate: null, parent: parent, taskstatus: ts, 
			maxrepeat: ts.maxrepeat, initstatus: ts.status, xmlvalues: ts.xmlvalues).save(failOnError: true)
	}
}
