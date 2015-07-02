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

		def messages = commandService.getMessages();

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

		render messages as JSON;
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

		render item as JSON;
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
		log.debug("JsonController.reply." + params.id + "." + params.reply)

		Queue queue = commandService.replyQueue(java.lang.Long.parseLong(params.id), params.reply)

		if(queue != null){
			render "{'result':'OK'}";
		} else {
			render "{'result':'queue is null'}";
		}

	}

	def List getTopTask(List list){
		List res = new ArrayList()
		for(Queue item : list){
			Date startdate = item.startdate
			Date enddate = item.enddate
			Date dt = new Date()

			if(Utils.isTimeInInterval(dt, startdate, enddate)){
				res.add(item)
			}
		}

		return res
	}

	def getchat() {
		log.debug("JsonController.getchat." + params.login + ";maxid=" + params.maxid + ";minid=" + params.minid)
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
			list = Chat.findAll("from Chat as a where (sendto=? or sendto='all') and (sendfrom=? or sendfrom='auto') order by a.id desc", [params.login, params.login], [max: 10])
			list = Utils.reverse(list);
		} else {
			//print "JsonController.getchat;maxid=" + maxid + ";minid=" + minid
			list = Chat.findAll("from Chat as a where (a.id > ? or a.id < ?) and (sendto=? or sendto='all') and (sendfrom=? or sendfrom='auto') order by a.id desc", [maxid, minid, params.login, params.login], [max: 10])
		}
		def List<ChatCommand> fullList = new ArrayList();
		for(Chat chat: list){
			ChatCommand chatCommand = new ChatCommand();
			chatCommand.setChat(chat)
			if("auto".equals(chat.sendfrom)){
				MessageCommand mc = new MessageCommand()
				mc.type = "Task"
				mc.status = "CMD"
				chatCommand.message = mc
			}
			fullList.add(chatCommand)
		}

		render fullList as JSON;
	}

	def sendchat() {
		log.debug("JsonController.sendchat." + params.login + "." + params.sendto + ".body=" + params.body)

		//def item = commandService.sendChat(params.login, params.sendto, params.body)
		def item = commandService.sendChat(params.login, "all", params.body)
		def List<Chat> list = new ArrayList();
		list.add(item)

		render list as JSON;
	}

}
