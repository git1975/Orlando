package com.igo.server

import java.text.SimpleDateFormat;

import grails.transaction.Transactional

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

		autoReply()

		repeatEveryProcess()
	}

	def autoReply(){
		List<Chat> items = Chat.findAll("from Chat as a order by a.id desc", [max: 1])
		if(items != null && items.size() > 0){
			if(items.get(0).sendto == 'user2'){
				sendChat("user2", "user1", "Согласен. Договорились")
			}
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

	def Queue replyQueue(long id, String reply) {
		Queue queue = Queue.find("from Queue as q where q.id = ?", [id])
		if(queue == null){
			return null;
		}
		queue.status = reply
		queue.save(failOnError: true)

		return queue
	}

	def Chat sendChat(String from, String to, String body) {
		Chat item = new Chat()

		Date now = new Date()

		item.sendfrom = from
		item.sendto = to
		item.body = body
		item.senddate = now
		item.sendtime = now.getTime()
		item.save(failOnError: true)

		return item
	}
}
