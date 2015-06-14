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
		if (request.method == 'GET') {
			def TaskStatus item = TaskStatus.get(params.id)

			if(item == null){
				redirect action: 'list'
			}
			println "Edit TaskStatus: " + item + ", " + item.buttons
			
			return [item: item, tasks: Task.list()]
		}
		def item = dataService.updateTaskStatus(TaskStatus.get(params.id), params.item_msgtext, params.msgtype, params.item_status, 
			params.item_lifetime, params.color, params.taskSelect)

		if (item.hasErrors()) {
			render view: 'edit', model: [item: item]
			return
		}
		redirect action: 'list'
	}

	def delete() {
		def TaskStatus item = TaskStatus.get(params.id)
		dataService.deleteTaskStatus(item.id);

		redirect action: 'list'
	}
}
