package com.igo.server

import grails.converters.JSON

class JsonController {
	def commandService
	//	static allowedMethods = [list:'GET',
	//		show:'GET',
	//		edit:['GET', 'POST'],
	//		save:'POST',
	//		update:['POST','PUT'],
	//		delete:['POST','DELETE']
	//	]

	def test() {
		render "Парамеры(param1):" + params.param1
	}

	def show() {
		String lastHash = params.hash
		log.debug("JsonController.show.hash=" + params.hash)

		def messages = commandService.getMessages()

		//Check last hash
		String hash = "";
		for(MessageCommand mes: messages){
			hash += "[" + mes.getId() + ";" + mes.getStatus() + ";" + mes.getType() + "]";
		}

		if(hash.equals(lastHash)){
			render "[]";
			return
		} else {
			if(messages.size() == 0){
				def mes = new MessageCommand()
				mes.type = "CLEAR"
				messages.add(mes)
				render messages as JSON;
				return
			}
		}

		render messages as JSON
	}

	def report() {
		String lastHash = params.hash
		log.debug("JsonController.report")

		Date now = new Date()
		Date processDate = new Date()

		//Определим время текущей задачи, для демо
		List<Queue> listCurrent = Queue.findAll("from Queue as q where q.type = 'Task' and ord = 1 order by startdate desc", [], [max: 1])
		if(listCurrent != null && listCurrent.size() > 0){
			Queue q = listCurrent.get(0)
			processDate = q.startdate
		}

		//Показать зеленым цветом инф. сообщение в любом случае, если текущее время между startdate и signaldate
		//Показать желтым цветом сообщение с запросом, если время текущее между signaldate и enddate
		//Показать красным цветом сообщение, если время текущее больше enddate и статус = TIMEOUT (т.е. пользователь не реагирует на запросы и система перевела статус в TIMEOUT)
		List<Queue> list = Queue.findAll("from Queue as q where q.type = 'Task' and startdate < ? and signaldate > ? and startdate >= ?", [now, now, processDate])

		//println "Green-" + listGreen.size + "-Yellow-" + listYellow.size

		def messages = new ArrayList();

		//Покажем инф. сообщение об этапах
		for(Queue q: list){
			def mes = commandService.getMessage(q, true)
			if(mes == null){
				mes = new MessageCommand()
				mes.type = "INFO"
				mes.body = "?"
			}

			messages.add(mes)
		}

		render messages as JSON;
	}

	def login() {
		log.debug("JsonController.login." + params.login  + "...")

		User item = User.find("from User as a where a.login = ? and password = ?", [params.login, params.password])

		if(item != null){
			print item.username + " login OK"
		}

		render "{'login':'" + item.login + "','role':'" + item.role.name + "','username':'" + item.username + "'}"
	}

	def taskCommit() {
		log.debug("JsonController.taskCommit." + params.id)

		Queue queue = commandService.commitQueue(java.lang.Long.parseLong(params.id))

		if(queue != null){
			render "{'result':'Task commited'}";
		} else {
			render "{'result':'Task not found'}";
		}

	}

	def reply() {
		log.debug("JsonController.reply." + params.id + "." + params.reply + "." + params.forStatus + "." + params.chatid)

		// Ищем элемент очереди с нужным id, на который еще не был дан ответ
		// обновляем его
		Queue queue = commandService.replyQueue(java.lang.Long.parseLong(params.id), params)

		def res = null;
		if(params.chatid != null && params.chatid != ""){
			commandService.replyChat(params.chatid, params.reply)

			if(queue != null){
				Button btn = Button.find("from Button where replystatus=?", [params.reply])
				if(btn != null){
					res = "{'result':'" + btn.name + "'}"					
				} else {
					res = "{'result':'OK'}"
				}
			} else {
				res = "{'result':'Просрочка'}"
			}
		}
		if(res != null){
			log.debug(res)
			render res
			return
		}

		if(queue != null){
			log.debug("{'result':'OK'}")
			render "{'result':'OK'}"
		} else {
			log.debug("{'result':'0'}")
			render "{'result':'0'}"
		}

	}

	def getchat() {
		log.debug("JsonController.getchat." + params.login + ";maxid=" + params.maxid + ";minid=" + params.minid + ".code=" + params.chatcode)
		def long maxid = 0
		try{
			maxid = Long.parseLong(params.maxid);
		} catch(Exception e){
			maxid = 0
		}
		def long minid = 0
		try{
			minid = Long.parseLong(params.minid);
		} catch(Exception e){
			minid = 0
		}
		if(minid < 0){
			minid = 1
		}

		def List<Chat> list;
		if(minid == 0 && maxid == 0){
			list = Chat.findAll("from Chat as a where ((chatcode=? and (sendto=? or sendto='all')) or chatcode=? or chatcode=?) order by a.id desc",
					[params.chatcode, params.login, params.login + "-" + params.chatcode, params.chatcode + "-" + params.login], [max: 10])
			list = Utils.reverse(list);
		} else {
			//print "JsonController.getchat;maxid=" + maxid + ";minid=" + minid
			list = Chat.findAll("from Chat as a where (a.id > ? or a.id < ?) and ((chatcode=? and (sendto=? or sendto='all')) or chatcode=? or chatcode=?) order by a.id desc",
					[maxid, minid, params.chatcode, params.login, params.login + "-" + params.chatcode, params.chatcode + "-" + params.login], [max: 10])
		}
		def List<ChatCommand> fullList = new ArrayList();
		for(Chat chat: list){
			ChatCommand chatCommand = new ChatCommand();
			chatCommand.setChat(chat)
			if(chat.replystatus != null){
				Button btn = Button.find("from Button where replystatus=?", [chat.replystatus])
				if(btn != null){
					chatCommand.message = "{'replytext':'" + btn.name + "'}";
				}
			}
			fullList.add(chatCommand)
		}

		log.debug(fullList as JSON)

		render fullList as JSON
	}
	
	def getchatstatus() {
		ChatStatus chatStatus = new ChatStatus(chatcode: params.chatcode, status: '1')
		
		Process process = Process.find("from Process where name=?", [params.chatcode])
		if(process == null){
			render chatStatus as JSON
			return
		}
		Queue queueTask = Queue.find("from Queue where idprocess=? and type='Task' and finished=0", [process.id])
		if(queueTask == null){
			render chatStatus as JSON
		}
		List<Queue> queue = Queue.findAll("from Queue where parent=? and type='Taskstatus'", [queueTask])
		for(Queue q: queue){
			if(q.status == 'DEADLINE'){
				chatStatus.status = '3'
				break
			}
			if(q.status == q.initstatus && q.taskstatus.msgtype == "CMD" && !q.finished  && q.repeatcount > 0){
				chatStatus.status = '2'
			}
		}
		
		render chatStatus as JSON
	}

	def sendchat() {
		log.debug("JsonController.sendchat." + params.login + "." + params.sendto + ".body=" + params.body + ".code=" + params.chatcode)

		//def item = commandService.sendChat(params.login, params.sendto, params.body)
		def item = commandService.sendChat(params.login, params.sendto, params.body, params.chatcode)
		def List<Chat> list = new ArrayList();
		list.add(item)

		render list as JSON
	}

	def getchats() {
		log.debug("JsonController.getchats." + params.login)
		
		def user = User.findByLogin(params.login)

		def List<ChatsCommand> fullList = new ArrayList();
		
		List<Process> list = Process.findAll("from Process where startdate is not null and accessgroup=?", [user.accessgroup])
		for(Process item: list){
			fullList.add(new ChatsCommand(code: item.name, name: item.description, ispersonal: false))
		}
		
		list = Process.findAll("from Process where startdate is null and accessgroup=?", [user.accessgroup])
		for(Process item: list){
			fullList.add(new ChatsCommand(code: item.name, name: item.description, ispersonal: false, ischild: true))
		}

		List<User> list2 = User.findAll("from User as a where a.login!=? and accessgroup=?", [params.login, user.accessgroup])
		for(User item: list2){
			fullList.add(new ChatsCommand(code: item.login, name: item.username, ispersonal: true))
		}

		render fullList as JSON
	}

	def startsubcase() {
		log.debug("JsonController.startsubcase." + params.login + ".process=" + params.process + ".parentchat=" + params.parentchat)
		
		commandService.startChildProcess(params.process, params.parentchat)
		
		ChatStatus chatStatus = new ChatStatus(chatcode: params.parentchat, status: 'Начат ' + Process.findByName(params.process).description)
		
		render chatStatus as JSON
	}
}
