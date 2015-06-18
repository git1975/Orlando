package com.igo.server

class ProcessController {

	def dataService

	def index() {
		redirect action: 'list'
	}
	
	def list() {
		return [items: Process.findAll()]
	}

	def add() {
		if (request.method == 'GET') {
			return [item: new Process()]
		}

		def item = dataService.createProcess(params.item_name, params.item_description, params.active, params.autostart, 
				params.item_repeatevery, params.item_startdate)

		if (item.hasErrors()) {
			return [item: item]
		}

		redirect action: 'list'
	}

	def edit() {
		if (request.method == 'GET') {
			def Process item = Process.get(params.id)

			if(item == null){
				redirect action: 'list'
			}
			log.debug("Edit: " + item)
			return [item: item]
		} else {
			String active = params.active
			
			log.debug("params.active=" + active)
			
			def item = dataService.updateProcess(Process.get(params.id), params.item_name, params.item_description, params.active, params.autostart, 
				params.item_repeatevery, params.item_startdate)
			if (item.hasErrors()) {
				render view: 'edit', model: [item: item]
				return
			}
			redirect action: 'list'
		}
	}

	def delete() {
		def Process item = Process.get(params.id)
		dataService.deleteProcess(item.id);

		redirect action: 'list'
	}
}
