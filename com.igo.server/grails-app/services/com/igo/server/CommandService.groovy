package com.igo.server

import java.text.SimpleDateFormat;

import grails.transaction.Transactional

@Transactional
class CommandService {
	private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

	def processNext() {
		print sdf.format(new Date()) + " processNext..."
		List list = Queue.findAll("from Queue as q where q.finished = ?", [false])
		for(Queue item : list){
			if("StartProcess".equals(item.getType())){
				processStartProcess(item)
			} else if("Task".equals(item.getType())){
				processTask(item)
			}
		}
	}

	def processStartProcess(Queue item) {
		print "processStartProcess." + item.getType() + "." + item.getFinished()

		ProcessInstanceFactory.createInstance(item.getIdprocess())
		item.setFinished(true)
		item.save(failOnError: true)
	}
	
	def processTask(Queue item) {
		
	}
}
