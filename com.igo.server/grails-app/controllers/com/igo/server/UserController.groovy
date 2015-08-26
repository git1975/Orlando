package com.igo.server

import grails.converters.JSON

class UserController {

	def dataService

	def list() {
		return [items: User.list()]
	}

	def add() {
		if (request.method == 'GET') {
			return [item: new User(), roles: Role.list(), accessgroup: Accessgroup.list()]
		}

		def user = dataService.createUser(params.item_login, params.item_username, params.item_password, params.roleSelect, params.accessgroupSelect)

		if (user.hasErrors()) {
			return [userBean: user]
		}

		redirect action: 'list'
	}

	def edit() {
		if (request.method == 'GET') {
			def User item = User.get(params.id)
			
			if(item == null){
				redirect action: 'list'
			}
			log.debug("Edit: " + item)
			return [item: item, roles: Role.list(), accessgroup: Accessgroup.list()]
		} else {
			def item = dataService.updateUser(User.get(params.id), params.item_login, params.item_username,
					params.item_password, params.roleSelect, params.accessgroupSelect)
			if (item.hasErrors()) {
				render view: 'edit'
				return
			}
			redirect action: 'list'
		}
	}

	def delete() {
		def User item = User.get(params.id)
		dataService.deleteUser(item.id);
		
		redirect action: 'list'
	}
}
