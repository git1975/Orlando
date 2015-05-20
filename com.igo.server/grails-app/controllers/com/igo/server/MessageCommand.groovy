package com.igo.server

import java.util.Date;

public class MessageCommand {
	public static final TYPE_CMD = "CMD"
	public static final REPLY_YESNO = "YESNO"
	public static final REPLY_HAND = "HAND"
	public static final REPLY_INFO = "REPLY_INFO"
	
	String id
	String name
	String type
	String replyVariants
	String body
	Date startdate
	Date enddate
	
	def setQueue(Queue q){
		this.id = q.id
		this.type = q.type
		this.name = q.description
		this.startdate = q.startdate
		this.enddate = q.enddate
	}

	static constraints={
		
	}
}
