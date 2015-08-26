package com.igo.server

class RoleController {

	def dataService

	def list() {
		return [items: Role.list()]
	}

	def add() {
		if (request.method == 'GET') {
			return [item: new Role()]
		}
		def item = dataService.createRole(params.item_code, params.item_name, params.item_description)

		if (item.hasErrors()) {
			return [item: item]
		}

		redirect action: 'list'
	}

	def edit() {
		if (request.method == 'GET') {
			def Role item = Role.get(params.id)

			if(item == null){
				redirect action: 'list'
			}
			return [item: item]
		} else {
			def item = dataService.updateRole(Role.get(params.id), params.item_code, params.item_name, params.item_description)
			if (item.hasErrors()) {
				render view: 'edit'
				return
			}
			redirect action: 'list'
		}
	}

	def delete() {
		def Role item = Role.get(params.id)
		dataService.deleteRole(item.id);
		
		redirect action: 'list'
	}
}
