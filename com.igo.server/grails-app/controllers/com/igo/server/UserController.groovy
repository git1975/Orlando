package com.igo.server

class UserController {

	def dataService

	def list() {
		return [items: User.list()]
	}

	def add() {
		if (request.method == 'GET') {
			return [item: new User(), roles: Role.list()]
		}

		def user = dataService.createUser(params.item_login, params.item_username, params.item_password, params.roleSelect)

		if (user.hasErrors()) {
			return [userBean: user]
		}

		redirect action: 'list'
	}

	def edit() {
		/*for(Iterator itr = params.iterator(); itr.hasNext();){
			String key = itr.next();
			println "->" + key
		}*/
		
		if (request.method == 'GET') {
			def User item = User.get(params.id)
			
			if(item == null){
				redirect action: 'list'
			}
			println "Edit: " + item
			return [item: item, roles: Role.list()]
		} else {
			def item = dataService.updateUser(User.get(params.id), params.item_login, params.item_username,
					params.item_password, params.roleSelect)
			if (item.hasErrors()) {
				render view: 'edit', model: [user: item]
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
