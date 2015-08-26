package com.igo.server

class AccessgroupController {

	def dataService

	def list() {
		return [items: Accessgroup.list()]
	}

	def add() {
		if (request.method == 'GET') {
			return [item: new Accessgroup()]
		}
		def item = dataService.createAccessgroup(params.item_code, params.item_name, params.item_description)

		if (item.hasErrors()) {
			return [item: item]
		}

		redirect action: 'list'
	}

	def edit() {
		if (request.method == 'GET') {
			def Accessgroup item = Accessgroup.get(params.id)

			if(item == null){
				redirect action: 'list'
			}
			return [item: item]
		} else {
			def item = dataService.updateAccessgroup(Accessgroup.get(params.id), params.item_code, params.item_name, params.item_description)
			if (item.hasErrors()) {
				render view: 'edit'
				return
			}
			redirect action: 'list'
		}
	}

	def delete() {
		def Accessgroup item = Accessgroup.get(params.id)
		dataService.deleteAccessgroup(item.id);
		
		redirect action: 'list'
	}
}
