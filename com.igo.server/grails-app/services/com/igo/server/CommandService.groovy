package com.igo.server

import java.text.SimpleDateFormat;

import grails.transaction.Transactional

@Transactional
class CommandService {
	private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	
	def autoStartProcess() {
		print sdf.format(new Date()) + " CommandService.autoStartProcess..."
		List list = Queue.findAll("from process as a where a.autostart = ?", [true])
		for(Process item : list){
			
		}
	}

	def processNext() {
		print sdf.format(new Date()) + " CommandService.processNext..."
		List list = Queue.findAll("from Queue as a where a.finished = ?", [false])
		for(Queue item : list){
			if("StartProcess".equals(item.getType())){
				processStartProcess(item)
			} else if("Task".equals(item.getType())){
				processTask(item)
			}
		}
	}

	def processStartProcess(Queue item) {
		print "CommandService.processStartProcess." + item.type + "." + item.finished

		(new ProcessInstanceFactory()).createInstance(item.getIdprocess())
		item.setFinished(true)
		item.save(failOnError: true)
	}
	
	def processTask(Queue item) {
		
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
		queue.status = status;
		queue.save(failOnError: true)
		
		return queue
	}
	
	def Queue replyQueue(long id, String reply) {
		Queue queue = Queue.find("from Queue as q where q.id = ?", [id])
		if(queue == null){
			return null;
		}		
		queue.status = reply;
		queue.save(failOnError: true)
		
		return queue
	}
}
