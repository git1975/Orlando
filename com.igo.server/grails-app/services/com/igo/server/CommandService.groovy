package com.igo.server

import java.text.SimpleDateFormat;

import grails.converters.JSON
import grails.transaction.Transactional

import java.util.Date;
import java.util.regex.Matcher
import java.util.regex.Pattern;

@Transactional
class CommandService {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

	def repeatEveryProcess() {
		List list = Process.findAll("from Process as a where a.repeatevery > 0")
		//Проверим, есть-ли на сегодня незастартованный процесс, или процесс стартованный с прошедшим временем > repeatevery
		for(Process item : list){
			List qList = Queue.findAll("from Queue as a where a.idprocess = ? and a.ord = 1 order by startdate desc", [item.id])
			if(qList == null || qList.size() == 0){
				processStartProcess(item)
			} else {
				for(Queue q : qList){
					Date dt = q.startdate
					long minutesAgo = Utils.dateMinutesInterval(dt, new Date())
					//log.debug("minutesAgo=" + minutesAgo)

					if(item.repeatevery < minutesAgo){
						processStartProcess(item)
					}

					break;
				}
			}
		}
	}

	def startProcessByTime() {
		List list = Process.findAll("from Process as a where a.repeatevery = 0")
		//Проверим, есть-ли в очереди задачи этого процесса с временем старта больше времени старта процесса
		for(Process item : list){
			Date nowTime = Utils.shiftDateInPresent(item.startdate);

			List qList = Queue.findAll("from Queue as a where a.idprocess = ? and a.ord = 1 and startdate > ?", [item.id, nowTime])
			if(qList == null || qList.size() == 0){
				processStartProcess(item)
			}
		}
	}

	def processNext() {
		//log.info(" CommandService.processNext...")
		List list = Queue.findAll("from Queue as a where a.finished = ?", [false])
		for(Queue item : list){
			if("StartProcess".equals(item.getType())){
				processStartProcess(item)
			} else if("Task".equals(item.getType())){
				//processTask(item)
			}
		}

		//autoReply()

		autoChat()

		repeatEveryProcess()

		startProcessByTime()
	}

	def autoReply(){
		List<Chat> items = Chat.findAll("from Chat as a order by a.id desc", [max: 1])
		if(items != null && items.size() > 0){
			if(items.get(0).sendto == 'user2'){
				sendChat("user2", "user1", "Согласен. Договорились")
			}
		}
	}

	def autoChat(){
		def messages = getMessages();
		for(MessageCommand mes: messages){

			if(!checkChatExists(mes)){
				log.println mes as JSON
				//this.sendChat("auto", mes.sendTo, mes.body, mes as JSON)
				Chat item = new Chat()

				Date now = new Date()

				item.sendfrom = "auto"
				item.sendto = mes.sendTo
				item.body = mes.body
				item.chatcode = mes.chatcode
				item.senddate = now
				item.sendtime = now.getTime()
				item.xmlcontent = (mes as JSON)
				item.save(failOnError: true)
			}
		}
	}

	def checkChatExists(MessageCommand mes){
		Chat item = Chat.find("from Chat as a where body=? order by a.id desc", [mes.body], [max: 1])
		if(item == null){
			return false;
		}

		Long time = item.sendtime
		Date dt = new Date();
		dt.setTime(time);

		long minutesAgo = Utils.dateMinutesInterval(dt, new Date())

		log.println minutesAgo

		if(minutesAgo > 2){
			return false;
		} else {
			return true;
		}
	}

	def processStartProcess(Queue item) {
		//log.info("CommandService.processStartProcess." + item.type + "." + item.finished)

		(new ProcessInstanceFactory()).createInstance(item.getIdprocess())
		item.setFinished(true)
		item.save(failOnError: true)
	}

	def processStartProcess(Process item) {
		//log.info("CommandService.processStartProcess." + item.id + "." + item.name)

		(new ProcessInstanceFactory()).createInstance(item.id)
		item.save(failOnError: true)
	}

	def processTask(Queue item) {
		//Если задача просрочена, то поставить статус TIMEOUT
		Date dt = item.getEnddate()
		if(item.status != 'TIMEOUT' && item.status == 'INIT' && Utils.isTimeInInterval(new Date(), dt, Utils.sdfTime.parse("235959+0300"))){
			statusQueue(item.id, 'TIMEOUT')
			log.debug('TIMEOUT Task ' + item.description)
		}
	}

	def Queue commitQueue(long id) {
		Queue queue = Queue.find("from Queue as q where q.id = ?", [id])
		if(queue == null){
			return null;
		}
		queue.finished = true;
		queue.status = "FINISH";
		queue.save(failOnError: true)

		return queue
	}

	def Queue statusQueue(long id, String status) {
		Queue queue = Queue.find("from Queue as q where q.id = ?", [id])
		if(queue == null){
			return null;
		}
		queue.status = status
		queue.save(failOnError: true)

		return queue
	}

	def Queue replyQueue(long id, String reply, String forStatus) {
		Queue queue = Queue.find("from Queue as q where q.id = ? and q.status=?", [id, forStatus])
		if(queue == null){
			return null;
		}
		queue.status = reply
		queue.save(failOnError: true)

		return queue
	}

	def Chat replyChat(String id, String replystatus) {
		Chat item = Chat.get(id)
		if(item == null){
			return null;
		}
		item.replystatus = replystatus
		item.save(failOnError: true)

		return item
	}

	def Chat sendChat(String from, String to, String body, String chatcode) {
		Chat item = new Chat()

		Date now = new Date()

		item.sendfrom = from
		item.sendto = to
		item.body = body
		item.chatcode = chatcode
		item.senddate = now
		item.sendtime = now.getTime()
		item.save(failOnError: true)

		return item
	}

	def ArrayList getMessages(){
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

		def messages = new ArrayList();

		//Покажем инф. сообщение о начале этапа
		for(Queue q: listGreen){
			def mes = getMessage(q, true)
			if(mes == null){
				mes = new MessageCommand()
				mes.type = "INFO"
				mes.sendTo = "all"
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
				mes.sendTo = "all"
				//mes.chatcode = 'production'
			}
			mes.setQueue(q)
			mes.body = "Просрочка"
			mes.type = "INFO"
			mes.status = "INIT"
			mes.color = 3

			messages.add(mes)
		}

		return messages;
	}

	def getMessage(Queue q, boolean isInfoStage){
		Task t = Task.find("from Task as a where a.id = ?", [q.task.id])
		Taskstatus ts
		if(isInfoStage){
			ts = Taskstatus.find("from Taskstatus as a where a.task=? and a.status=? and a.msgtype='INFO' and a.status='INIT'", [t, q.status])
		} else {
			ts = Taskstatus.find("from Taskstatus as a where a.task=? and a.status=? and ((a.msgtype!='INFO' and a.status='INIT')or(a.status!='INIT'))", [t, q.status])
		}
		//найден статус задачи, занесенный в шаблон
		if(ts != null){
			def mes = new MessageCommand()
			mes.setQueue(q)
			mes.id = q.id
			mes.body = ts.msgtext
			mes.type = ts.msgtype
			mes.status = q.status
			mes.forStatus = ts.status
			mes.color = ts.color
			mes.sendTo = ts.sendTo
			Process process = Process.get(q.idprocess)
			mes.chatcode = process.name
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

	def doResetDatabase(){
		try{
			Chat.findAll().each { it.delete(flush:true, failOnError:true) }			
			Queue.findAll().each { it.delete(flush:true, failOnError:true) }
			
			return 'OK'
		} catch (Exception e){
			return e.getMessage()
		}
	}
}
