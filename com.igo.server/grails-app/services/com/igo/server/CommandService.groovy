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
			Date now = new Date()
			//Проверим, не пора-ли стартовать процесс
			if(nowTime.before(now)){
				List qList = Queue.findAll("from Queue as a where a.idprocess = ? and a.ord = 1 and startdate > ?", [item.id, nowTime])
				if(qList == null || qList.size() == 0){
					processStartProcess(item)
				}
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

			initDatabase()
			return 'OK'
		} catch (Exception e){
			return e.getMessage()
		}
	}

	def initDatabase(){
		if(!Role.count) {
			Role role1 = new com.igo.server.Role(name: 'head', description: 'Директор').save(failOnError: true)
			Role role2 = new com.igo.server.Role(name: 'mgr', description: 'Управляющий').save(failOnError: true)
		}
		if(!User.count) {
			Role role1 = Role.find("from Role as a where a.name = ?", ['head'])
			Role role2 = Role.find("from Role as a where a.name = ?", ['mgr'])

			User usr1 = new User(login: 'user1', username: 'Директор Иванов', password: '1', role: role1).save(failOnError: true)
			User usr2 = new User(login: 'user2', username: 'Управляющий Петров', password: '1', role: role2).save(failOnError: true)
			User usr3 = new User(login: 'user3', username: 'Жорж Милославский', password: '1', role: role2).save(failOnError: true)
		}
		if(!Process.count) {
			//Repeating process
			Process proc = new Process(name: 'production', description: 'производство', repeatevery: 0)
			.save(failOnError: true)
		}
		if(!Task.count) {
			Process proc = Process.find("from Process as a where a.name = ?", ['production'])

			User usr1 = User.find("from User as a where a.login = ?", ['user1'])
			User usr2 = User.find("from User as a where a.login = ?", ['user2'])
			User usr3 = User.find("from User as a where a.login = ?", ['user3'])

			SimpleDateFormat sdfTime = new SimpleDateFormat("mm")

			Task task = new Task(name: 'start', description: 'старт', user: usr1, ord: 1, startdate: sdfTime.parse("00"), signaldate: sdfTime.parse("01"), enddate: sdfTime.parse("01"), process: proc).save(failOnError: true)
			//proc.addToTasks(task).save(failOnError: true)
			task = new Task(name: 'prepare', description: 'подготовка', user: usr2, ord: 2, startdate: sdfTime.parse("01"), signaldate: sdfTime.parse("01"), enddate: sdfTime.parse("02"), process: proc).save(failOnError: true)
			//proc.addToTasks(task).save(failOnError: true)
			task = new Task(name: 'running', description: 'исполнение', user: usr3, ord: 3, startdate: sdfTime.parse("02"), signaldate: sdfTime.parse("02"), enddate: sdfTime.parse("03"), process: proc).save(failOnError: true)
			//proc.addToTasks(task).save(failOnError: true)
			task = new Task(name: 'finish', description: 'завершение', user: usr3, ord: 4, startdate: sdfTime.parse("03"), signaldate: sdfTime.parse("04"), enddate: sdfTime.parse("05"), process: proc).save(failOnError: true)
			//proc.addToTasks(task).save(failOnError: true)
		}
		if(!Queue.count) {
			//Process proc = Process.findByName("production")
			//Queue queue = new Queue(type: 'StartProcess', finished: false, description: 'description', idprocess: proc.id)
			//.save(failOnError: true)
		}
		if(!Deviation.count) {
			new Deviation(name: 'Отказ').save(failOnError: true)
		}
		if(!Button.count) {
			new Button(code: 'YES', name: 'Да', replystatus: 'REPLY_YES').save(failOnError: true)
			new Button(code: 'NO', name: 'Нет', replystatus: 'REPLY_NO').save(failOnError: true)
			new Button(code: 'FINISH', name: 'Нет', replystatus: 'REPLY_FINISH').save(failOnError: true)
			new Button(code: 'HAND', name: 'Ручной режим', replystatus: 'REPLY_HAND').save(failOnError: true)
			new Button(code: 'BTN_1', name: 'Хорошо', replystatus: 'REPLY_1').save(failOnError: true)
			new Button(code: 'BTN_2', name: 'Плохо', replystatus: 'REPLY_2').save(failOnError: true)
		}
		if(!Taskstatus.count) {
			Button btn1 = Button.find("from Button as a where a.code = ?", ['YES'])
			Button btn2 = Button.find("from Button as a where a.code = ?", ['FINISH'])
			Button btn3 = Button.find("from Button as a where a.code = ?", ['BTN_1'])
			Button btn4 = Button.find("from Button as a where a.code = ?", ['BTN_2'])

			Taskstatus ts1, ts2, ts3, ts4

			//start
			Task task1 = Task.find("from Task as a where a.name = ?", ['start'])
			ts1 = new Taskstatus(status: 'INIT', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Начинается цикл <process>, под контролем <user=user1>').save(failOnError: true)
			ts1.task = task1
			//prepare
			task1 = Task.find("from Task as a where a.name = ?", ['prepare'])
			ts1 = new Taskstatus(status: 'INIT', msgtype: 'CMD', sendTo: 'user2', color: 2, msgtext: 'Этап <stage>. Продолжить выполнение?').save(failOnError: true)
			ts1.addToButtons(btn1)
			ts1.addToButtons(btn2)
			ts1.task = task1
			ts2 = new Taskstatus(status: 'REPLY_YES', msgtype: 'INFO', sendTo: 'user2', color: 1, msgtext: 'Штатный режим этапа <stage>').save(failOnError: true)
			ts2.task = task1
			ts3 = new Taskstatus(status: 'REPLY_FINISH', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Досрочный финиш этапа <stage>').save(failOnError: true)
			ts3.task = task1
			//running
			task1 = Task.find("from Task as a where a.name = ?", ['running'])
			ts1 = new Taskstatus(status: 'INIT', msgtype: 'CMD', sendTo: 'user2', color: 2, msgtext: 'Этап <stage>. Продолжить выполнение?').save(failOnError: true)
			ts1.addToButtons(btn1)
			ts1.addToButtons(btn2)
			ts1.task = task1
			ts2 = new Taskstatus(status: 'REPLY_YES', msgtype: 'INFO', sendTo: 'user2', color: 1, msgtext: 'Штатный режим этапа <stage>').save(failOnError: true)
			ts2.task = task1
			ts3 = new Taskstatus(status: 'REPLY_FINISH', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Досрочный финиш этапа <stage>').save(failOnError: true)
			ts3.task = task1
			//finish
			task1 = Task.find("from Task as a where a.name = ?", ['finish'])
			ts1 = new Taskstatus(status: 'INIT', msgtype: 'INFO', sendTo: 'user2', color: 1, msgtext: 'Завершается цикл <process>').save(failOnError: true)
			ts1.task = task1
			ts2 = new Taskstatus(status: 'INIT', msgtype: 'CMD', sendTo: 'user2', color: 2, msgtext: 'Оцените исполнение цикла <process>?').save(failOnError: true)
			ts2.addToButtons(btn3)
			ts2.addToButtons(btn4)
			ts2.task = task1
			ts3 = new Taskstatus(status: 'REPLY_1', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Оценка цикла <process> Хорошо').save(failOnError: true)
			ts3.task = task1
			ts4 = new Taskstatus(status: 'REPLY_2', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Оценка цикла <process> Плохо').save(failOnError: true)
			ts4.task = task1
		}
	}
}
