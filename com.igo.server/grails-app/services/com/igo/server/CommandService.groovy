package com.igo.server

import java.util.Iterator;

import java.text.SimpleDateFormat;

import grails.converters.JSON
import grails.transaction.Transactional

import java.util.Date;
import java.util.regex.Matcher
import java.util.regex.Pattern;

@Transactional
class CommandService {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	def initDbService

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

	/**
	 * Если в очереди нет задач процесса с временем старта больше времени старта процесса, то создать задачи процесса
	 * @return
	 */
	def startProcessByTime() {
		List process = Process.findAll("from Process as a where a.repeatevery = 0 and startdate is not null")
		//Проверим, есть-ли в очереди задачи этого процесса с временем старта больше времени старта процесса
		for(Process item : process){
			Date nowTime = Utils.shiftDateInPresent(item.startdate);
			Date now = new Date()
			//Проверим, не пора-ли стартовать процесс
			if(nowTime.before(now)){
				List queue = Queue.findAll("from Queue where type='Task' and idprocess=? and ord=1 and startdate>?", [item.id, nowTime])
				if(queue == null || queue.size() == 0){
					ProcessInstanceFactory.createInstance(item.id)
				}
			}
		}
	}
	
	/**
	 * Стартовать процесс в контексте целевого чата
	 * @param idprocess
	 * @param idparent
	 * @return
	 */
	def startChildProcess(String process, String parentchat) {
		Process p = Process.find("from Process where name=?", [process])
		ProcessInstanceFactory.createParentInstance(p.id, parentchat)
	}

	/**
	 * Это начальный метод, который вызывает Qurtz каждые 5 сек
	 * @return
	 */
	def processNext() {
		//Завершаем задачи по времени
		finishTaskByTime()
		//Создадим экземпляр процесса - таски в очереди
		startProcessByTime()
		//Для активного экземпляра таска найдем соответствующие статусу этого таска сообщения (Taskstatus)
		//и создадим экземпляры сообщений (в Queue)
		createTaskstatusInstances()
		//Обработать экземпляры сообщений - создать сообщения чата для активных сообщений, проверить deadline и др. проверки
		createTaskstatusChat()
	}

	def processNextOld() {
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

	/**
	 * Завершаем задачи по времени
	 * и отправляем отчеты, настроенные на отправку по завершению задачи
	 */
	def finishTaskByTime(){
		Date now = new Date()
		List tasks = Queue.findAll("from Queue where type = 'Task' and finished = ? and enddate < ?", [false, now])
		for(Queue item : tasks){
			item.finished = true
			item.save(failOnError: true)
			
			//Необходимо отправить отчеты по завершению задачи
			createTaskstatusInstance(item, 'END_TASK')
		}
	}

	/**
	 * Для активного экземпляра таска найдем соответствующие статусу этого таска сообщения (Taskstatus) 
	 * и создадим экземпляры сообщений (в Queue)
	 * @return
	 */
	def createTaskstatusInstances() {
		Date now = new Date()
		List tasks = Queue.findAll("from Queue where type = 'Task' and finished = ? and startdate < ? and enddate > ?", [false, now, now])
		for(Queue item : tasks){
			createTaskstatusInstance(item, null)
		}
	}
	
	/**
	 * Физически создает экземпляры таскстатусов для таска с проверкой, что они не созданы
	 * @param item
	 */
	private void createTaskstatusInstance(Queue item, String status) {
		if(status == null || "".equals(status)){
			status = item.status
		}
		List taskstatus = Taskstatus.findAll("from Taskstatus where task = ? and status = ?", [item.task, status])
		for(Taskstatus ts : taskstatus){
			Queue q = Queue.find("from Queue where type = 'Taskstatus' and parent = ? and taskstatus = ?", [item, ts])
			if(q == null){
				ProcessInstanceFactory.createTaskstatusInstance(item, ts)
			}
		}
	}

	/**
	 * Отправим сообщения в чат согласно активным экземплярам очереди type = 'Taskstatus'
	 * сразу завершаем задачи INFO
	 * Для задач CMD увеличиваем счетчик repeatcount каждый раз при посылке повторного сообщения
	 * @return
	 */
	def createTaskstatusChat(){
		//Сообщения со статусом 'DEADLINE' уже не рассматриваются
		List tasks = Queue.findAll("from Queue as a where a.finished = ? and type = 'Taskstatus' and status != 'DEADLINE'", [false])
		for(Queue item : tasks){
			MessageCommand mes = getTaskstatusMessage(item)
			if(mes != null){
				//mes.buttons.each{b->log.debug(b.name)}
				//log.debug(mes.type)
				if(item.taskstatus.msgtype == 'INFO'){
					sendChatMessage(mes)

					Date now = new Date()
					item.signaldate = now
					item.finished = true
					item.save(failOnError: true)
				} else if(item.taskstatus.msgtype == 'REPORT'){
					sendChatMessage(mes)

					Date now = new Date()
					item.signaldate = now
					item.finished = true
					item.save(failOnError: true)
				} else if(item.taskstatus.msgtype == 'CMD'){
					//Проверим, настало-ли время отправлять повторно
					int repeatcount = item.repeatcount					

					Date now = new Date()
					Date signal = item.signaldate
					if(signal == null){
						signal = now
					}
					long minutesAgo = Utils.dateMinutesInterval(signal, now)
					log.debug('minutesAgo=' + minutesAgo)
					if(item.taskstatus.repeatevery > 0 && minutesAgo >= item.taskstatus.repeatevery){
						repeatcount++
						if(repeatcount > item.maxrepeat){
							item.status = 'DEADLINE'
							item.save(failOnError: true)
						} else {
							if(repeatcount > 1){
								mes.body += ' (Повтор)'
							}
							sendChatMessage(mes)

							item.repeatcount = repeatcount
							item.signaldate = now

							item.save(failOnError: true)
						}
					}

				}
			}
		}
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
				log.debug mes as JSON
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

		//log.println minutesAgo

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

		ProcessInstanceFactory.createInstance(item.id)
		//item.save(failOnError: true)
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
	
	def Queue replyQueue(long id, Map<Object, Object> params) {
		String reply = params.reply 
		String forStatus = params.forStatus 
		Queue queue = Queue.find("from Queue as q where q.id = ? and q.status=?", [id, forStatus])
		if(queue == null){
			return null;
		}
		
		//Если привязан регистр, то найти значение регистра из параметров и записать это новое значение в queue
		String reg = queue.registers
		Map<String, String> regMap = Utils.splitToMap(reg)
		boolean b = false 
		if(reg != null && reg != ''){			
			for (Iterator<String> itr = regMap.keySet().iterator(); itr.hasNext();) {
				String item = itr.next();
				// Если среди параметров от клиента есть текущий регистр, то перезаписать его новым значением
				if(params.get(item) != null){
					regMap.put(item, params.get(item))	
					b = true
				}
			}			
		}
		// Регистр был
		if(b){
			queue.registers = Utils.mapToString(regMap)
		}
		queue.status = reply
		queue.finished = true
		queue.save(failOnError: true)
		if(queue.parent != null){
			if(queue.parent.status == forStatus){
				queue.status = reply
				//TODO Смержить значения регистров
				queue.parent.registers = queue.registers
				queue.parent.save(failOnError: true)
			}
		}

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
	
	def Chat sendChatMessage(MessageCommand mes) {
		Chat item = new Chat()

		Date now = new Date()
		String xmlcontent = null
		JSON.use('deep'){
			xmlcontent = (mes as JSON)
		}

		item.senddate = now
		item.sendtime = now.getTime()
		item.sendfrom = "auto"
		item.sendto = mes.sendTo
		item.body = mes.body
		item.chatcode = mes.chatcode
		item.xmlcontent = xmlcontent

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
		List<Queue> listRed = Queue.findAll("from Queue as q where q.type = 'Task' and enddate < ? and q.status = 'INIT' and startdate >= ?", [now, processDate])

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
			//Показываем сообщение о просрочке только для задач CMD
			Task task = q.task
			Taskstatus ts = Taskstatus.find("from Taskstatus where task=?", [task])
			if(ts != null && ts.msgtype == "CMD"){
				def mes = getMessage(q, true)
				if(mes == null){
					mes = new MessageCommand()
					mes.type = "INFO"
					mes.sendTo = "all"
					Process p = Process.get(q.idprocess)
					mes.chatcode = p.name
				}
				mes.setQueue(q)
				mes.body = "Нет ответа по задаче " + q.description
				mes.type = "INFO"
				mes.status = "INIT"
				mes.color = 3

				messages.add(mes)}
		}

		return messages;
	}

	def getMessage(Queue q, boolean isInfoStage){
		Taskstatus ts
		//println "isInfoStage=" + isInfoStage + "-" + t.id
		if(isInfoStage){
			ts = Taskstatus.find("from Taskstatus as a where a.task=? and a.msgtype='INFO' and a.status='INIT'", [q.task])
		} else {
			ts = Taskstatus.find("from Taskstatus as a where a.task=? and a.status=? and ((a.msgtype!='INFO' and a.status='INIT')or(a.status!='INIT'))", [q.task, q.status])
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

	def getTaskstatusMessage(Queue q){
		Taskstatus ts = q.taskstatus
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
			mes.registers = ts.registers
			if(q.parentchat == null || "".equals(q.parentchat)){
				Process process = Process.get(q.idprocess)
				mes.chatcode = process.name
			} else {
				mes.chatcode = q.parentchat
			}
			mes.buttons = new Button[ts.buttons.size()]
			for(int i = 0; i < ts.buttons.size(); i++){
				mes.buttons[i] = ts.buttons[i]
				if(mes.buttons[i].register != null){
					Map reg = Utils.splitToMap(q.registers)					
					mes.buttons[i].register.value = reg.get(mes.buttons[i].register.code)
				}
			}

			mes.body = getMessageBody(q, mes.body)

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
			} else if(s.indexOf('<reg=') >= 0){
				// получить значение регистра
				int ix = s.indexOf('=');
				def code = s.substring(ix + 1, s.length() - 1)
				// ищем значение регистра у родительского таска				
				s = Utils.getValueFromPair(q.parent.registers, code)
			} else if(s.indexOf('<bonus=') >= 0){
				// Вычислить бонус
				int bonus = 0
				int ix = s.indexOf('=');
				def login = s.substring(ix + 1, s.length() - 1)
				User user = User.find("from User as a where a.login = ?", [login])
				// ищем значение repeatcount среди всех сообщений CMD для пользователя
				List<Queue> list = Queue.findAll("from Queue where type='Taskstatus' and parent=? and taskstatus.msgtype='CMD' and user=?", [q.parent, user])
				for(Queue item: list){
					// Проверим, совпадает-ли значение регистра в Taskstatus и в Queue
					if(item.registers == item.taskstatus.registers){
						bonus++
					} else {
						bonus--
					}
					// Проверим число повторений
					if(item.maxrepeat > 0 && item.repeatcount > 1){
						bonus -= (item.repeatcount - 1)
					}
				}
				
				s = bonus.toString()
			}

			result += s
		}

		return result
	}

	/*
	 * drop table chat;
	 drop table taskstatus;
	 drop table taskstatus_button;
	 drop table task_deviation;
	 drop table button;
	 drop table deviation;
	 drop table queue;
	 drop table task;
	 drop table process;
	 drop table user;
	 drop table role;
	 delete from chat where id > 0;
	 delete from taskstatus where id > 0;
	 delete from taskstatus_button where id > 0;
	 delete from task_deviation where id > 0;
	 delete from button where id > 0;
	 delete from deviation where id > 0;
	 delete from task where id > 0;
	 delete from queue where id > 0;
	 delete from process where id > 0;
	 delete from user where id > 0;
	 delete from role where id > 0;
	 */
	def doResetDatabase(){
		try{
			Taskstatus.findAll().each { it.buttons = null; it.save(flush:true, failOnError:true) }
			Taskstatus.findAll().each { it.delete(flush:true, failOnError:true) }
			Button.findAll().each { it.delete(flush:true, failOnError:true) }
			Deviation.findAll().each { it.delete(flush:true, failOnError:true) }
			Chat.findAll().each { it.delete(flush:true, failOnError:true) }
			Queue.findAll().each { it.delete(flush:true, failOnError:true) }
			Task.findAll().each { it.delete(flush:true, failOnError:true) }
			Process.findAll().each { it.delete(flush:true, failOnError:true) }
			User.findAll().each { it.delete(flush:true, failOnError:true) }
			Role.findAll().each { it.delete(flush:true, failOnError:true) }

			initDbService.initDatabase()
			return 'OK'
		} catch (Exception e){
			return e.getMessage()
		}
	}

	def doResetDatabaseDemo(){
		try{
			Taskstatus.findAll().each { it.buttons = null; it.save(flush:true, failOnError:true) }
			Taskstatus.findAll().each { it.delete(flush:true, failOnError:true) }
			Button.findAll().each { it.delete(flush:true, failOnError:true) }
			Deviation.findAll().each { it.delete(flush:true, failOnError:true) }
			Chat.findAll().each { it.delete(flush:true, failOnError:true) }
			Queue.findAll().each { it.delete(flush:true, failOnError:true) }
			Task.findAll().each { it.delete(flush:true, failOnError:true) }
			Process.findAll().each { it.delete(flush:true, failOnError:true) }
			User.findAll().each { it.delete(flush:true, failOnError:true) }
			Role.findAll().each { it.delete(flush:true, failOnError:true) }

			initDbService.initDatabaseDemo()
			return 'OK'
		} catch (Exception e){
			return e.getMessage()
		}
	}

	def doResetChatAndQueue(){
		try{
			Chat.findAll().each { it.delete(flush:true, failOnError:true) }
			Queue.findAll().each { it.delete(flush:true, failOnError:true) }

			return 'OK'
		} catch (Exception e){
			return e.getMessage()
		}
	}
}
