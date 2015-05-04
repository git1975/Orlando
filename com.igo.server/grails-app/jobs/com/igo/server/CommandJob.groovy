package com.igo.server

/**
 * 
 * cronExpression: "s m h D M W Y"
 | | | | | | `- Year [optional]
 | | | | | `- Day of Week, 1-7 or SUN-SAT, ?
 | | | | `- Month, 1-12 or JAN-DEC
 | | | `- Day of Month, 1-31, ?
 | | `- Hour, 0-23
 | `- Minute, 0-59
 `- Second, 0-59
 */
class CommandJob {
	static triggers = {
		//cron name: 'myTrigger', cronExpression: "*/5 * * * * ?"
		simple name:'simpleTrigger', startDelay:1000, repeatInterval: 3000, repeatCount: -1
	}

	def group = "Job Group"
	def description = "Auto job Trigger"

	def execute(){
		print new Date() + "-Job run"
		List unprocessed = Queue.executeQuery("from Queue where completed = 0")
//		for(Object item : unprocessed){
//			print item.toString()
//		}
	}
}
