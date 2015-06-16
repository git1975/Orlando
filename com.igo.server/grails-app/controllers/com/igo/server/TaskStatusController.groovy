package com.igo.server

class TaskStatusController {

	def dataService

	def list() {
		return [items: TaskStatus.list()]
	}

	def add() {
		if (request.method == 'GET') {
			return [item: new TaskStatus(), tasks: Task.list()]
		}

		def item = dataService.createTaskStatus(params.item_msgtext, params.msgtype, params.item_status, params.item_lifetime, params.color,
				params.taskSelect)

		if (item.hasErrors()) {
			render view: 'add', model: [item: item]
		}

		redirect action: 'list'
	}

	def edit() {
		/*for(Iterator itr = params.iterator(); itr.hasNext();){
		 String key = itr.next();
		 println "->" + key
		 }*/

		if (request.method == 'GET') {
			println "edit GET"

			def TaskStatus item = TaskStatus.get(params.id)
			def allbuttons = Button.findAll("from Button where code is not null")

			if(item == null){
				redirect action: 'list'
			}

			List btns = session["buttons"]
			if(btns == null){
				btns = new ArrayList(1);
				btns.addAll(item.buttons)
				session["buttons"] = btns
			}

			return [item: item, tasks: Task.list(), buttons: btns, allbuttons: allbuttons]
		}
		println "edit POST"

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
		
		def item = dataService.updateTaskStatus(TaskStatus.get(params.id), params.item_msgtext, params.msgtype, params.item_status,
				params.item_lifetime, params.color, params.taskSelect, session["buttons"])

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
		def TaskStatus item = TaskStatus.get(params.id)
		dataService.deleteTaskStatus(item.id);

		redirect action: 'list'
	}

	def delButton() {
		String mainid = params.mainid
		String id = params.id

		//def TaskStatus item = TaskStatus.get(params.id)
		//dataService.deleteTaskStatusButton(mainid, params.id);
		List btns = session["buttons"]
		for(Button btn: btns){
			println "->id=" + id + ",btn.id=" + btn.id
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
		println "addButton"

		String mainid = params.mainid

		def btn = Button.findByCode("YES")
		btn.id = 0

		List btns = session["buttons"]
		btns.add(btn)

		redirect action: 'edit', id: mainid
	}
}
