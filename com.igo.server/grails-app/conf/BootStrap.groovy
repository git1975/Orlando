class BootStrap {

	def init = { servletContext ->
		if(!com.igo.server.Process.count) {
			com.igo.server.Process proc = new com.igo.server.Process(name: 'production', description: 'производство')
			.save(failOnError: true)
			com.igo.server.Task task = new com.igo.server.Task(name: 'start', description: 'старт').save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new com.igo.server.Task(name: 'verify', description: 'верификация').save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new com.igo.server.Task(name: 'prepare', description: 'подготовка').save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new com.igo.server.Task(name: 'running', description: 'исполнение').save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new com.igo.server.Task(name: 'finish', description: 'завершение').save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
		}
	}
	def destroy = {
	}
}
