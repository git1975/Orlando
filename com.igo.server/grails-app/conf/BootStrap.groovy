import com.igo.server.Button
import com.igo.server.Deviation
import com.igo.server.Role
import com.igo.server.Task
import com.igo.server.TaskStatus
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
			
			Task task = new Task(name: 'start', description: 'старт', user: usr1, ord: 1).save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new Task(name: 'verify', description: 'верификация', user: usr2, ord: 2).save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new Task(name: 'prepare', description: 'подготовка', user: usr2, ord: 3).save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new Task(name: 'running', description: 'исполнение', user: usr3, ord: 4).save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
			task = new Task(name: 'finish', description: 'завершение', user: usr3, ord: 5).save(failOnError: true)
			proc.addToTasks(task).save(failOnError: true)
		}
		if(!Queue.count) {
			Process proc = Process.findByName("production")
			Queue queue = new Queue(type: 'StartProcess', finished: false, description: 'description', idprocess: proc.id)
			.save(failOnError: true)
		}
		if(!Deviation.count) {
			new Deviation(name: 'Отказ').save(failOnError: true)
		}
		if(!Button.count) {
			new Button(code: 'YES', name: 'Да', replystatus: 'REPLY_YES').save(failOnError: true)
			new Button(code: 'NO', name: 'Нет', replystatus: 'REPLY_NO').save(failOnError: true)
			new Button(code: 'HAND', name: 'Ручной режим', replystatus: 'REPLY_HAND').save(failOnError: true)
		}
		if(!TaskStatus.count) {
			Button btn1 = Button.find("from Button as a where a.code = ?", ['YES'])
			Button btn2 = Button.find("from Button as a where a.code = ?", ['NO'])
			Button btn3 = Button.find("from Button as a where a.code = ?", ['HAND'])
			
			//start
			Task task1 = Task.find("from Task as a where a.name = ?", ['start'])
			TaskStatus ts1 = new TaskStatus(status: 'INIT', msgtype: 'CMD', sendTo: 'Manager', msgtext: 'подтверди контроль производства').save(failOnError: true)
			ts1.addToButtons(btn1)
			ts1.addToButtons(btn2)
			ts1.task = task1
			TaskStatus ts2 = new TaskStatus(status: 'REPLY_NO', msgtype: 'CMD', sendTo: 'Director', msgtext: 'назначь управляющего производством на сегодня').save(failOnError: true)
			ts2.addToButtons(btn3)
			ts2.addToButtons(btn2)
			ts2.task = task1
			TaskStatus ts3 = new TaskStatus(status: 'REPLY_HAND', msgtype: 'INFO', sendTo: 'Director, Manager', msgtext: 'сегодня производство управляется в ручном режиме').save(failOnError: true)
			ts3.task = task1
		}
	}
	def destroy = {
	}
}
