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

		//print list.toString();
		//list = 	getTopTask(list)

		def messages = new ArrayList();

		//Находим таск в статусе INIT
		for(Queue q: list){
			//int id = q.ord
			Task t = Task.find("from Task as a where a.id = ?", [q.task.id])
			TaskStatus ts = TaskStatus.find("from TaskStatus as a where a.task = ? and a.status = ?", [t, q.status])
			//найден статус задачи, занесенный в шаблон
			if(ts != null){
				Date dt = q.getEnddate()
				if(q.getSignaldate() != null){
					dt = q.getSignaldate()
				}
				//print '---->>>>' + t.name
				//print '---->>>>' + Utils.sdfTime.parse("151900+0300")
				//print '---->>>>' + Utils.sdfTime2.format(dt) + "+0300"
				//print '---->>>>' + Utils.sdfTime.parse("235959+0300")
				//Отправлять сигнал пользователю. если наступило время сигнала
				if(Utils.isTimeInInterval(new Date(), Utils.sdfTime.parse(Utils.sdfTime2.format(dt) + "+0300"), Utils.sdfTime.parse("235959+0300"))){
					print '---->>>>isTimeInInterval=true'
					
					def mes = new MessageCommand()
					mes.setQueue(q)
					mes.body = ts.msgtext
					mes.type = ts.msgtype
					mes.status = q.status
					mes.buttons = new Button[ts.buttons.size()]
					for(int i = 0; i < ts.buttons.size(); i++){
						mes.buttons[i] = ts.buttons[i]
					}
					messages.add(mes)
				}
			}
		}

		render messages as JSON;
	}

	def login() {
		print "JsonController.login." + params.login  + "..."

		com.igo.server.User item = com.igo.server.User.find("from User as a where a.login = ? and password = ?", [params.login, params.password])
		
		if(item != null){
			print item.username + " login OK"
		}

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
	
	def getchat() {
		print "JsonController.getchat." + params.login + ";maxid=" + params.maxid + ";minid=" + params.minid
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
			list = Chat.findAll("from Chat as a order by a.id desc", [max: 10])
			list = Utils.reverse(list);
		} else {
			//print "JsonController.getchat;maxid=" + maxid + ";minid=" + minid
			list = Chat.findAll("from Chat as a where a.id > ? or a.id < ? order by a.id desc", [maxid, minid], [max: 10])
		}
		
		
		render list as JSON;
	}
	
	def sendchat() {
		print "JsonController.sendchat." + params.login + "." + params.to + ".body=" + params.body

		def item = commandService.sendChat(params.login, params.to, params.body)
		def List<Chat> list = new ArrayList();
		list.add(item)

		render list as JSON;
	}

}
