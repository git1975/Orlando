package com.igo.server

class Chat {
	String sendfrom
	String sendto
	Date senddate
	String body

    byte[] picture

    static constraints = {
        picture(nullable:true)
    }
    
    static mapping = {
        picture column: "picture", sqlType: "blob"
    }

}
