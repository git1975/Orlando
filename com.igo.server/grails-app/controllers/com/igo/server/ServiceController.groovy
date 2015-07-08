package com.igo.server

class ServiceController {
	def commandService

    def index() { }
	
	def reset() {
		def res = commandService.doResetDatabase()
		
		render res
	}
	
	def resetDemo() {
		def res = commandService.doResetDatabaseDemo()
		
		render res
	}
	
	def resetChat() {
		def res = commandService.doResetChatAndQueue()
		
		render res
	}
}
