package com.igo.server

import java.util.Date;

class TaskController {

	def dataService

	def list() {
		return [items: Task.findAll("from Task order by process, ord")]
	}

	def add() {
		if (request.method == 'GET') {
			return [item: new Task(), process: Process.list()]
		}
		def item = dataService.createTask(params.item_name, params.item_description, params.item_ord, 
			params.item_startdate, params.item_signaldate, params.item_enddate, params.processSelect)

		if (item.hasErrors()) {
			return [itemBean: item]
		}

		redirect action: 'list'
	}

	def edit() {
		/*for(Iterator itr = params.iterator(); itr.hasNext();){
			String key = itr.next();
			println "->" + key
		}*/
		
		if (request.method == 'GET') {
			def Task item = Task.get(params.id)
			
			if(item == null){
				redirect action: 'list'
			}
			log.debug("Edit: " + item)
			return [item: item, process: Process.list()]
		} else {
			def item = dataService.updateTask(Task.get(params.id), params.item_name, params.item_description, params.item_ord, 
			params.item_startdate, params.item_signaldate, params.item_enddate, params.processSelect)
			if (item.hasErrors()) {
				render view: 'edit', model: [item: item]
				return
			}
			redirect action: 'list'
		}
	}

	def delete() {
		def Task item = Task.get(params.id)
		dataService.deleteTask(item.id);
		
		redirect action: 'list'
	}
}
