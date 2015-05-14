import com.igo.server.Deviation
import com.igo.server.Role
import com.igo.server.Task
import com.igo.server.User
import com.igo.server.Process
import com.igo.server.Queue

class BootStrap {

	def init = { servletContext ->
		if(!Role.count) {
			Role role1 = new com.igo.server.Role(name: 'head', description: 'Директор').save(failOnError: true)
			Role role2 = new com.igo.server.Role(name: 'mgr', description: 'Управляющий').save(failOnError: true)
		}
		if(!User.count) {
			Role role1 = Role.find("from Role as a where a.name = ?", ['head'])
			Role role2 = Role.find("from Role as a where a.name = ?", ['mgr'])
			
			User usr1 = new User(login: 'user1', username: 'Шпак Антон Семёнович', password: '1', role: role1).save(failOnError: true)
			User usr2 = new User(login: 'user2', username: 'Горбунков Семён Семёныч', password: '1', role: role2).save(failOnError: true)
			User usr3 = new User(login: 'user3', username: 'Жорж Милославский', password: '1', role: role2).save(failOnError: true)
		}
		if(!Process.count) {
			Process proc = new Process(name: 'production', description: 'производство', autostart: true, isrepeat: true)
			.save(failOnError: true)
			
			User usr1 = User.find("from User as a where a.login = ?", ['user1'])
			User usr2 = User.find("from User as a where a.login = ?", ['user2'])
			User usr3 = User.find("from User as a where a.login = ?", ['user3'])
			
			Task task = new Task(name: 'start', description: 'старт', user: usr1).save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new Task(name: 'verify', description: 'верификация', user: usr2).save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new Task(name: 'prepare', description: 'подготовка', user: usr2).save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new Task(name: 'running', description: 'исполнение', user: usr3).save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new Task(name: 'finish', description: 'завершение', user: usr3).save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
		}
		if(!Queue.count) {
			Queue queue = new Queue(type: 'StartProcess', finished: false, description: 'description', idprocess: 1)
			.save(failOnError: true)
		}
		if(!Deviation.count) {
			new Deviation(name: 'Отказ').save(failOnError: true)
		}
	}
	def destroy = {
	}
}
