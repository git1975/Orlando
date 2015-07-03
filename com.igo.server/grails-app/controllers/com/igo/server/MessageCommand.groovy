package com.igo.server

import java.text.SimpleDateFormat;
import java.util.Date;
import groovy.json.JsonSlurper

public class MessageCommand {
	public static SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-ddHH:mm:ssZ");
	
	String id
	String name
	String type
	String body
	String status
	String sendTo
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
	
	def getDt1(){
		if(startdate != null){
			return sdfFull.format(startdate)
		} else {
			return "";
		}
	}
	
	def getDt2(){
		if(enddate != null){
			return sdfFull.format(enddate)
		} else {
			return "";
		}
	}
}
