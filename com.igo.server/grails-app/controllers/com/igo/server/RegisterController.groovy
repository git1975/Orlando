package com.igo.server

class RegisterController {

	def dataService

	def list() {
		return [items: Register.list()]
	}

	def add() {
		if (request.method == 'GET') {
			return [item: new Register()]
		}

		def item = dataService.createRegister(params.item_code, params.item_name, params.item_description)

		if (item.hasErrors()) {
			return [item: item]
		}

		redirect action: 'list'
	}

	def edit() {
		if (request.method == 'GET') {
			def item = Register.get(params.id)

			if(item == null){
				redirect action: 'list'
			}
			log.debug("Edit Register: " + item)
			return [item: item]
		} else {
			def item = dataService.updateRegister(Register.get(params.id), params.item_code, params.item_name, params.item_description)
			if (item.hasErrors()) {
				render view: 'edit', model: [user: item]
				return
			}
			redirect action: 'list'
		}
	}

	def delete() {
		def item = Register.get(params.id)
		dataService.deleteRegister(item.id);
		
		redirect action: 'list'
	}
}
