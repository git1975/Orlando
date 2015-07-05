package com.igo.server

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatCommand {
	Long id
	String sendfrom
	String sendto
	Date senddate
	Long sendtime
	String body
	String message
	String chatcode

	def setChat(Chat chat){
		sendfrom = chat.sendfrom
		sendto = chat.sendto
		senddate = chat.senddate
		sendtime = chat.sendtime
		body = chat.body
		id = chat.id
		message = chat.xmlcontent
		chatcode = chat.chatcode
	}
}
