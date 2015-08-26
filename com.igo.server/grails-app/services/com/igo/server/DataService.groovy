package com.igo.server

import java.text.SimpleDateFormat;
import java.util.Date;

import grails.transaction.Transactional

@Transactional
class DataService {

	def createUser(String login, String username, String password, String role, String accessgroup){
		Role r = Role.find("from Role where name=?", [role])

		def user = new User(login: login, username: username, password: password, role: Role.findByName(role), accessgroup: Accessgroup.findByCode(accessgroup))
		user.save(failOnError: true)
		user
	}

	def updateUser(User user, String login, String username, String password, String role, String accessgroup){
		if(user != null){
			user.login = login
			user.username = username
			user.password = password
			user.role = Role.findByName(role)
			user.accessgroup = Accessgroup.findByCode(accessgroup)
			user.save(failOnError: true)
			user
		}
	}

	def deleteUser(long id){
		def user = User.get(id)
		if(user != null){
			user.delete(flush: true)
		}
	}

	def createButton(String code, String name, String replystatus, String register){
		def item = new Button(code: code, name: name, replystatus: replystatus, register: Register.findByCode(register))
		item.save(failOnError: true)
		item
	}

	def updateButton(Button item, String code, String name, String replystatus, String register){
		if(item != null){
			item.code = code
			item.name = name
			item.replystatus = replystatus
			item.register = Register.findByCode(register)
			item.save(failOnError: true)
			item
		}
	}

	def deleteButton(long id){
		def item = Button.get(id)
		if(item != null){
			item.delete(flush: true)
		}
	}

	def createTaskStatus(String msgtext, String msgtype, String status, String lifetime, String maxrepeat, String repeatevery, String color, String registers, String task){
		Task r = Task.find("from Task where name=?", [task])

		log.debug("createTaskStatus-" + msgtext+'-'+msgtype+'-'+status+'-'+lifetime+'-'+color+'-'+task)

		def item = new Taskstatus(msgtext: msgtext, msgtype: msgtype, status: status, lifetime: Integer.parseInt(lifetime),
		color: Integer.parseInt(color), registers: registers, task: r, maxrepeat: maxrepeat, repeatevery: repeatevery)
		item.save(failOnError: true)
		item
	}

	def updateTaskStatus(Taskstatus item, String msgtext, String msgtype, String status, String lifetime, String maxrepeat, String repeatevery, String color, String registers, String task, List buttons, String sendTo){
		if(item != null){
			Task r = Task.find("from Task where name=?", [task])

			log.debug("updateTaskStatus-" + msgtext+'-'+msgtype+'-'+status+'-'+lifetime+'-'+color+'-'+task)

			item.msgtext = msgtext
			item.msgtype = msgtype
			item.status = status
			item.maxrepeat = Integer.parseInt(maxrepeat)
			item.repeatevery = Integer.parseInt(repeatevery)
			item.lifetime = Integer.parseInt(lifetime)
			item.color = Integer.parseInt(color)
			item.registers = registers
			item.sendTo = sendTo
			item.task = r
			//Buttons
			Iterator<Object> i = buttons.iterator();
			while (i.hasNext()) {
				Button btn = i.next();
				if(btn.id == 0){
					i.remove();
				}
			}
			item.buttons = buttons

			item.save(failOnError: true)
			item
		}
	}

	def deleteTaskStatus(long id){
		def item = Taskstatus.get(id)
		if(item != null){
			item.delete(flush: true)
		}
	}

	def deleteTaskStatusButton(String mainid, String id){
		def item = Taskstatus.get(mainid)
		if(item != null){
			for(Button btn: item.buttons){
				if(id.equals(btn.id.toString())){
					item.buttons.remove(btn)

					log.debug("deleteTaskStatusButton:" + mainid + "," + id)

					item.save(flush: true)

					break
				}
			}
		}
	}

	//2015-06-04 12:05:22.0
	static SimpleDateFormat sdfFull2 = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss")

	def createProcess(String name, String description, String active, String autostart, String repeatevery, String startdate, String accessgroup){
		def item = new Process(name: name, description: description, active: Boolean.parseBoolean(active),
		autostart: Boolean.parseBoolean(autostart), repeatevery: repeatevery, startdate: sdfFull2.parse(startdate), accessgroup: Accessgroup.findByCode(accessgroup))
		item.save(failOnError: true)
		item
	}

	def updateProcess(Process item, String name, String description, String active, String autostart, String repeatevery, String startdate, String accessgroup){
		if(item != null){
			item(name: name, description: description, active: Boolean.parseBoolean(active),
			autostart: Boolean.parseBoolean(autostart), repeatevery: repeatevery, startdate: sdfFull2.parse(startdate), accessgroup: Accessgroup.findByCode(accessgroup))

			item.save(failOnError: true)
			item
		}
	}

	def deleteProcess(long id){
		def item = Process.get(id)
		if(item != null){
			item.delete(flush: true)
		}
	}

	def createTask(String name, String description, String ord, String startdate, String signaldate, String enddate, String process){
		def item = new Task(name: name, description: description, ord: Integer.parseInt(ord), startdate: sdfFull2.parse(startdate),
		signaldate: sdfFull2.parse(signaldate), enddate: sdfFull2.parse(enddate), process: Process.findByName(process))
		item.save(failOnError: true)
		item
	}

	def updateTask(Task item, String name, String description, String ord, String startdate, String signaldate, String enddate, String process){
		if(item != null){
			item(name: name, description: description, ord: Integer.parseInt(ord), startdate: sdfFull2.parse(startdate),
			signaldate: sdfFull2.parse(signaldate), enddate: sdfFull2.parse(enddate), process: Process.findByName(process))save(failOnError: true)
			item
		}
	}

	def deleteTask(long id){
		def item = Task.get(id)
		if(item != null){
			item.delete(flush: true)
		}
	}

	def createRegister(String code, String name, String description){
		def item = new Register(code: code, name: name, description: description)
		item.save(failOnError: true)
		item
	}

	def updateRegister(Register item, String code, String name, String description){
		if(item != null){
			item.code = code
			item.name = name
			item.description = description
			item.save(failOnError: true)
			item
		}
	}

	def deleteRegister(long id){
		def item = Register.get(id)
		if(item != null){
			item.delete(flush: true)
		}
	}

	def createAccessgroup(String code, String name, String description){
		def item = new Accessgroup(code: code, name: name, description: description)
		item.save(failOnError: true)
		item
	}

	def updateAccessgroup(Accessgroup item, String code, String name, String description){
		if(item != null){
			item.code = code
			item.name = name
			item.description = description
			item.save(failOnError: true)
			item
		}
	}

	def deleteAccessgroup(long id){
		def item = Accessgroup.get(id)
		if(item != null){
			item.delete(flush: true)
		}
	}

}
