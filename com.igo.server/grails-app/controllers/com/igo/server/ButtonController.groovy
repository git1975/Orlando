package com.igo.server

class ButtonController {

	def dataService

	def list() {
		return [items: Button.list()]
	}

	def add() {
		if (request.method == 'GET') {
			return [item: new Button()]
		}

		def item = dataService.createButton(params.item_code, params.item_name, params.item_replystatus, params.registerSelect)

		if (item.hasErrors()) {
			return [item: item]
		}

		redirect action: 'list'
	}

	def edit() {
		if (request.method == 'GET') {
			def Button item = Button.get(params.id)

			if(item == null){
				redirect action: 'list'
			}
			log.debug("Edit Button: " + item)
			return [item: item, register: Register.list()]
		} else {
			def item = dataService.updateButton(Button.get(params.id), params.item_code, params.item_name, params.item_replystatus, params.registerSelect)
			if (item.hasErrors()) {
				render view: 'edit', model: [user: item]
				return
			}
			redirect action: 'list'
		}
	}

	def delete() {
		def Button item = Button.get(params.id)
		dataService.deleteButton(item.id);
		
		redirect action: 'list'
	}
}
