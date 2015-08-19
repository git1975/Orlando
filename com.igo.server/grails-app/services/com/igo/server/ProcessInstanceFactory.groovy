package com.igo.server

class ProcessInstanceFactory {
	def static createParentInstance(long idprocess, Process parent){
		Queue process = createInstance(idprocess)
		if(process != null){
			process(parent: parent)
			process.save(failOnError: true)
		}
	}
	/**
	 * Создает экземпляры тасков в таблице Queue. Начальный статус всегда 'INIT'
	 * @param idprocess
	 * @return
	 */
	def static Queue createInstance(long idprocess){
		Queue process = null
		Process item = Process.find("from Process as p where p.id = ? and p.active = 1", [idprocess])
		if(item != null){
			//Создадим экземпляр процесса
			process = new Queue(type: 'Process', finished: false, description: item.description, idprocess: item.id, 
				startdate: new Date()).save(failOnError: true)
			
			List<Task> tasks = Task.findAll("from Task where process=?", [item])
			for(Task t: tasks){
				Date startdt = t.startdate
				Date signaldt = t.signaldate
				Date enddt = t.enddate
				Date now = new Date();
				startdt = Utils.addMinutes(now, t.startdate)
				signaldt = Utils.addMinutes(now, t.signaldate)
				enddt = Utils.addMinutes(now, t.enddate)
				Queue msg = new Queue(type: 'Task', finished: false, parent: process, description: t.description, idprocess: idprocess, ord: t.ord, user: t.getUser(), task: t, status: 'INIT',
				startdate: startdt, signaldate: signaldt, enddate: enddt)
				.save(failOnError: true)
			}
		}
		return process
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

		Queue msg = new Queue(type: 'Taskstatus', finished: false, description: ts.task.description + ' <' + ts.msgtype + '>', 
			idprocess: ts.task.process.id, ord: 0, user: user, task: ts.task, status: ts.status,
			startdate: new Date(), signaldate: new Date(), enddate: null, parent: parent, taskstatus: ts, 
			maxrepeat: ts.maxrepeat, initstatus: ts.status, registers: ts.registers).save(failOnError: true)
	}
}
