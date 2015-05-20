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

	def show() {
		print "JsonController.show"

		List<Queue> list = Queue.findAll("from Queue as q where q.finished = ? and q.type = 'Task' order by ord", [false])

		print list.toString();
		//list = 	getTopTask(list)

		def messages = new ArrayList();

		//Ветка стартовой задачи
		Queue qInit = null
		//Находим стартовый таск в статусе INIT
		for(Queue q: list){
			//Если найден, то проинформировать менеджера, сообщением YES_NO
			if(q.ord == 1 && q.status == "INIT"){
				def mes = new MessageCommand()
				mes.setQueue(q)
				mes.body = "Подтвердите контроль производства"
				mes.type = MessageCommand.TYPE_CMD
				mes.replyVariants = MessageCommand.REPLY_YESNO
				messages.add(mes)
			}
			//владелец таска отвечает Да
			if(q.ord == 1 && q.status == "REPLY_YES"){
				//commandService.commitQueue(java.lang.Long.parseLong(q.id))
			}
			//владелец таска отвечает Нет
			if(q.ord == 1 && q.status == "REPLY_NO"){
				def mes = new MessageCommand()
				mes.setQueue(q)
				mes.body = "Назначь управляющего производством на сегодня"
				mes.type = MessageCommand.TYPE_CMD
				mes.replyVariants = MessageCommand.REPLY_HAND
				messages.add(mes)
			}
			//Если перевели в ручной режим, то всем отвечаем 
			if(q.ord == 1 && q.status == "REPLY_HAND"){
				def mes = new MessageCommand()
				mes.setQueue(q)
				mes.body = "Управление производством находится в ручном режиме"
				mes.type = MessageCommand.TYPE_CMD
				mes.replyVariants = MessageCommand.REPLY_INFO
				messages.add(mes)
			}
		}

		/*for(Queue q: list){
		 if(q.status == "INIT"){
		 def mes = new MessageCommand()
		 mes.setQueue(q)
		 mes.body = "Вам необходимо подтвердить задачу"
		 mes.type = MessageCommand.TYPE_CMD
		 mes.replyVariants = MessageCommand.REPLY_YESNO
		 messages.add(mes)
		 } else if(q.status == "N"){
		 def mes = new MessageCommand()
		 mes.setQueue(q)
		 mes.body = "назначь управляющего производством на сегодня – ФИО"
		 mes.type = MessageCommand.TYPE_CMD
		 mes.replyVariants = MessageCommand.REPLY_YESNO
		 messages.add(mes)
		 }
		 }*/

		render messages as JSON;
	}

	def login() {
		print "JsonController.login." + params.login + "@" + params.password

		com.igo.server.User item = com.igo.server.User.find("from User as a where a.login = ? and password = ?", [params.login, params.password])

		render item as JSON;
	}

	def taskCommit() {
		print "JsonController.taskCommit." + params.id

		Queue queue = commandService.commitQueue(java.lang.Long.parseLong(params.id))

		if(queue != null){
			render "{'result':'Task commited'}";
		} else {
			render "{'result':'Task not found'}";
		}

	}

	def reply() {
		print "JsonController.reply." + params.id + "." + params.reply

		Queue queue = commandService.replyQueue(java.lang.Long.parseLong(params.id), params.reply)

		if(queue != null){
			render "{'result':'OK'}";
		} else {
			render "{'result':'queue is null'}";
		}

	}

	def List<Queue> getTopTask(List<Queue> list){
		List<Queue> res = new ArrayList<Queue>()
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

}
