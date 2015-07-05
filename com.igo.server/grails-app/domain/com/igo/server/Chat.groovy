package com.igo.server

class Chat {
	String sendfrom
	String sendto
	Date senddate
	Long sendtime
	String body
	String xmlcontent
	String replystatus

    byte[] picture

    static constraints = {
        picture(nullable:true)
		xmlcontent size: 1..5000
    }
    
    static mapping = {
        picture column: "picture", sqlType: "blob"
		senddate column: "senddate", sqlType: "timestamp"
    }
	
}
