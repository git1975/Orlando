package com.igo.server

import java.util.Date;

public class MessageCommand {	
	String id
	String name
	String type
	String body
	String status
	Date startdate
	Date enddate
	Button[] buttons
	int color
	
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
