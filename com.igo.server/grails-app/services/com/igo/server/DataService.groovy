package com.igo.server

import java.text.SimpleDateFormat;

import grails.transaction.Transactional

@Transactional
class DataService {

	def createUser(String login, String username, String password, String role){
		Role r = Role.find("from Role where name=?", [role])

		def user = new User(login: login, username: username, password: password, role: r)
		user.save(failOnError: true)
		user
	}

	def updateUser(User user, String login, String username, String password, String role){
		if(user != null){
			Role r = Role.find("from Role where name=?", [role])

			user.login = login
			user.username = username
			user.password = password
			user.role = r
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

	def createButton(String code, String name, String replystatus){
		def item = new Button(code: code, name: name, replystatus: replystatus)
		item.save(failOnError: true)
		item
	}

	def updateButton(Button item, String code, String name, String replystatus){
		if(item != null){
			item.code = code
			item.name = name
			item.replystatus = replystatus
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

	def createTaskStatus(String msgtext, String msgtype, String status, String lifetime, String color, String task){
		Task r = Task.find("from Task where name=?", [task])

		log.debug("createTaskStatus-" + msgtext+'-'+msgtype+'-'+status+'-'+lifetime+'-'+color+'-'+task)

		def item = new Taskstatus(msgtext: msgtext, msgtype: msgtype, status: status, lifetime: Integer.parseInt(lifetime),
		color: Integer.parseInt(color), task: r)
		item.save(failOnError: true)
		item
	}

	def updateTaskStatus(Taskstatus item, String msgtext, String msgtype, String status, String lifetime, String color, String task, List buttons){
		if(item != null){
			Task r = Task.find("from Task where name=?", [task])

			log.debug("updateTaskStatus-" + msgtext+'-'+msgtype+'-'+status+'-'+lifetime+'-'+color+'-'+task)

			item.msgtext = msgtext
			item.msgtype = msgtype
			item.status = status
			item.lifetime = Integer.parseInt(lifetime)
			item.color = Integer.parseInt(color)
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
	
	def createProcess(String name, String description, String active, String autostart, String repeatevery, String startdate){
		def item = new Process(name: name, description: description, active: Boolean.parseBoolean(active), 
			autostart: Boolean.parseBoolean(autostart), repeatevery: repeatevery, startdate: sdfFull2.parse(startdate))
		item.save(failOnError: true)
		item
	}

	def updateProcess(Process item, String name, String description, String active, String autostart, String repeatevery, String startdate){
		if(item != null){
			item.name = name
			item.description = description
			item.active = Boolean.parseBoolean(active)
			item.autostart = Boolean.parseBoolean(autostart)
			item.repeatevery = Long.parseLong(repeatevery)
			item.startdate = sdfFull2.parse(startdate)
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
}
