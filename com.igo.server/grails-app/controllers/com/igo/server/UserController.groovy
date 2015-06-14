package com.igo.server

class UserController {
	
	def dataService

    def list() {
		return [users: User.list()]
	}
	
	def add() {
		if (request.method == 'GET') {
			return [user: new User()]
		}

		def user = dataService.createUser(params.user_username, params.user_password)

		if (user.hasErrors()) {
			return [userBean: user]
		}

		//redirect action: 'add'
	}
	
	def edit() {
		def owner = Owner.get(params.id)
		if (request.method == 'GET') {
			render view: 'add', model: [ownerBean: owner]
			return
		}

		petclinicService.updateOwner(Owner.get(params.id), params.owner?.firstName, params.owner?.lastName,
				params.owner?.address, params.owner?.city, params.owner?.telephone)

		if (owner.hasErrors()) {
			render view: 'add', model: [ownerBean: owner]
			return
		}

		redirect action: 'show', id: owner.id
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
