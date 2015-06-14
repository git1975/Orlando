package com.igo.server

class UserController {

	def dataService

	def list() {
		return [users: User.list()]
	}

	def add() {
		if (request.method == 'GET') {
			return [user: new User(), roles: Role.list()]
		}

		def user = dataService.createUser(params.user_login, params.user_username, params.user_password)

		if (user.hasErrors()) {
			return [userBean: user]
		}

		redirect action: 'list'
	}

	def edit() {
		for(Iterator itr = params.iterator(); itr.hasNext();){
			String key = itr.next();
			println "->" + key
		}
		
		if (request.method == 'GET') {
			def User item = User.get(params.id)
			
			if(item == null){
				redirect action: 'list'
			}
			println "Edit: " + item
			return [user: item, roles: Role.list()]
		} else {
			def item = dataService.updateUser(User.get(params.id), params.user_login, params.user_username,
					params.user_password, params.roleSelect)
			if (item.hasErrors()) {
				render view: 'edit', model: [user: item]
				return
			}
			redirect action: 'list'
		}
	}

	def delete() {
		for(User user : User.list()){
			Object obj = params.get("users." + user.id)
			if(obj != null){
				if("on".equals(obj.toString())){
					System.out.println "Delete user: " + user;
					dataService.deleteUser(user.id);
				}
			}
		}
		redirect action: 'list'
	}
}
