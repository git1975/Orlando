package com.igo.server

import grails.converters.JSON
import java.util.regex.Matcher
import java.util.regex.Pattern;

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
		println "JsonController.show.hash=" + params.hash

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
		List<Queue> listGreen = Queue.findAll("from Queue as q where q.type = 'Task' and startdate < ? and signaldate > ? and startdate >= ?", [now, now, processDate])
		List<Queue> listYellow = Queue.findAll("from Queue as q where q.type = 'Task' and signaldate < ? and enddate > ? and startdate >= ?", [now, now, processDate])
		List<Queue> listRed = Queue.findAll("from Queue as q where q.type = 'Task' and enddate < ? and q.status = 'INIT' and startdate >= ? and ord != 1 and ord != 4", [now, processDate])

		//println "Green-" + listGreen.size + "-Yellow-" + listYellow.size

		def messages = new ArrayList();

		//Покажем инф. сообщение о начале этапа
		for(Queue q: listGreen){
			def mes = getMessage(q, true)
			if(mes == null){
				mes = new MessageCommand()
				mes.type = "INFO"
			}

			messages.add(mes)
		}
		//Покажем основные сообщения между signaldate и enddate
		for(Queue q: listYellow){
			def mes = getMessage(q, false)
			if(mes != null){
				messages.add(mes)
			}
		}
		//Покажем сообщение о просрочке
		for(Queue q: listRed){
			def mes = getMessage(q, true)
			if(mes == null){
				mes = new MessageCommand()
				mes.type = "INFO"
			}
			mes.setQueue(q)
			mes.body = "Просрочка"
			mes.type = "INFO"
			mes.status = "INIT"
			mes.color = 3
			messages.add(mes)
		}

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

	def getMessage(Queue q, boolean isInfoStage){
		Task t = Task.find("from Task as a where a.id = ?", [q.task.id])
		TaskStatus ts
		if(isInfoStage){
			ts = TaskStatus.find("from TaskStatus as a where a.task=? and a.status=? and a.msgtype='INFO' and a.status='INIT'", [t, q.status])
		} else {
			ts = TaskStatus.find("from TaskStatus as a where a.task=? and a.status=? and ((a.msgtype!='INFO' and a.status='INIT')or(a.status!='INIT'))", [t, q.status])
		}
		//найден статус задачи, занесенный в шаблон
		if(ts != null){
			def mes = new MessageCommand()
			mes.setQueue(q)
			mes.body = ts.msgtext
			mes.type = ts.msgtype
			mes.status = q.status
			mes.color = ts.color
			mes.buttons = new Button[ts.buttons.size()]
			for(int i = 0; i < ts.buttons.size(); i++){
				mes.buttons[i] = ts.buttons[i]
			}

			mes.body = getMessageBody(q, mes.body)

			//Check lifetime
			if(mes.type == "CMD"){
				long diff = (new Date()).getTime() - q.signaldate.getTime()
				long diffMinutes = Math.round(diff/1000)
				if(diffMinutes >= ts.lifetime*60){
					mes.color = 3
				}
			}
			return mes
		}
		return null
	}
	
	def report() {
		String lastHash = params.hash
		println "JsonController.report"

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
			def mes = getMessage(q, true)
			if(mes == null){
				mes = new MessageCommand()
				mes.type = "INFO"
				mes.body = "?"
			}

			messages.add(mes)
		}

		render messages as JSON;
	}

	/**
	 * @param  Queue q
	 * @param String body
	 * @return
	 */
	def getMessageBody(Queue q, String body){
		Pattern pattern = Pattern.compile('(<.*?>)|(.+?(?=<|$))');
		Matcher matcher = pattern.matcher(body);
		def result = ''
		while (matcher.find()) {
			def s = matcher.group()
			if('<process>'.equals(s)){
				Process process = Process.find("from Process as a where a.id = ?", [q.idprocess])
				s = process.description
			} else if('<stage>'.equals(s)){
				s = q.description
			} else if(s.indexOf('<user=') >= 0){
				int ix = s.indexOf('=');
				def login = s.substring(ix + 1, s.length() - 1)
				User user = User.find("from User as a where a.login = ?", [login])
				if(user != null){
					s = user.username
				} else {
					s = "?"
				}
			}

			result += s
		}

		return result
	}

	def login() {
		println "JsonController.login." + params.login  + "..."

		User item = User.find("from User as a where a.login = ? and password = ?", [params.login, params.password])

		if(item != null){
			print item.username + " login OK"
		}

		render item as JSON;
	}

	def taskCommit() {
		println "JsonController.taskCommit." + params.id

		Queue queue = commandService.commitQueue(java.lang.Long.parseLong(params.id))

		if(queue != null){
			render "{'result':'Task commited'}";
		} else {
			render "{'result':'Task not found'}";
		}

	}

	def reply() {
		println "JsonController.reply." + params.id + "." + params.reply

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
		println "JsonController.getchat." + params.login + ";maxid=" + params.maxid + ";minid=" + params.minid
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
		println "JsonController.sendchat." + params.login + "." + params.sendto + ".body=" + params.body

		def item = commandService.sendChat(params.login, params.sendto, params.body)
		def List<Chat> list = new ArrayList();
		list.add(item)

		render list as JSON;
	}

}
