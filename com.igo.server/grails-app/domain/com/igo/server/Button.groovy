package com.igo.server

class Button {
	String code
	String name
	String replystatus
	Register register

    static constraints = {
		code blank: false, unique: true
    }
}
