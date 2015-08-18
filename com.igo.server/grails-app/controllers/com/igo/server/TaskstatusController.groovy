package com.igo.server

class TaskstatusController {

	def dataService

	def list() {
		return [items: Taskstatus.list()]
	}

	def add() {
		if (request.method == 'GET') {
			def users = User.findAll("from User")
			def allUser = new User();
			allUser.login = "all"
			allUser.username = "Все"
			users.add(allUser)
			
			return [item: new Taskstatus(), tasks: Task.list(), users: users]
		}

		def item = dataService.createTaskStatus(params.item_msgtext, params.msgtype, params.item_status, params.item_lifetime, params.item_maxrepeat, params.item_repeatevery, 
			params.color, params.item_registers, params.taskSelect)

		if (item.hasErrors()) {
			render view: 'add', model: [item: item]
		}

		redirect action: 'list'
	}

	def edit() {
		if (request.method == 'GET') {
			log.debug("edit GET")

			def Taskstatus item = Taskstatus.get(params.id)
			def allbuttons = Button.findAll("from Button where code is not null")
			def users = User.findAll("from User")
			def allUser = new User();
			allUser.login = "all"
			allUser.username = "Все"
			users.add(allUser)

			if(item == null){
				redirect action: 'list'
			}

			List btns = session["buttons"]
			if(btns == null){
				btns = new ArrayList(1);
				btns.addAll(item.buttons)
				session["buttons"] = btns
			}

			return [item: item, tasks: Task.list(), buttons: btns, allbuttons: allbuttons, users: users]
		}
		log.debug("edit POST")

		//Добавленные кнопки
		List btns = session["buttons"]
		params.entrySet().findAll {
			it.key.startsWith("allbuttons.")
		}.each { 
			def code = "$it.value"	
			Button btn = Button.find("from Button where code=?", [code])
			if(btn != null){
				btns.add(btn)
			}
		}
		
		session["buttons"] = btns
		
		def item = dataService.updateTaskStatus(Taskstatus.get(params.id), params.item_msgtext, params.msgtype, params.item_status,
				params.item_lifetime, params.item_maxrepeat, params.item_repeatevery, params.color, params.item_registers, params.taskSelect, session["buttons"], params.sendTo)

		session["buttons"] = null

		if (item.hasErrors()) {
			render view: 'edit', model: [item: item]
			return
		}
		redirect action: 'list'
	}

	def cancel() {
		session["buttons"] = null

		redirect action: 'list'
	}

	def delete() {
		def Taskstatus item = Taskstatus.get(params.id)
		dataService.deleteTaskStatus(item.id);

		redirect action: 'list'
	}

	def delButton() {
		log.debug("delButton")
		String mainid = params.mainid
		String id = params.id

		//def TaskStatus item = TaskStatus.get(params.id)
		//dataService.deleteTaskStatusButton(mainid, params.id);
		List btns = session["buttons"]
		for(Button btn: btns){
			if(id.equals(btn.id.toString())){
				btns.remove(btn)
				println "->remove:" + btns
				session["buttons"] = btns
				break
			}
		}

		redirect action: 'edit', id: mainid
	}

	def addButton() {
		log.debug("addButton")

		String mainid = params.mainid

		def btn = Button.findByCode("YES")
		btn.id = 0

		List btns = session["buttons"]
		btns.add(btn)

		redirect action: 'edit', id: mainid
	}
}
