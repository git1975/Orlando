package com.igo.server

class ServiceController {
	def commandService

    def index() { }
	
	def reset() {
		def res = commandService.doResetDatabase()
		
		render res
	}
}
