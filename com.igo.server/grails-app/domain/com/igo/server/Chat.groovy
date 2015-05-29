package com.igo.server

class Chat {
	String sendfrom
	String sendto
	Date senddate
	Long sendtime
	String body

    byte[] picture

    static constraints = {
        picture(nullable:true)
    }
    
    static mapping = {
        picture column: "picture", sqlType: "blob"
		senddate column: "senddate", sqlType: "timestamp"
    }
	
}
