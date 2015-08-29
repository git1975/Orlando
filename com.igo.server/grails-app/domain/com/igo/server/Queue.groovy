package com.igo.server

import java.util.Date
import java.util.Map;

class Queue {
	Date startdate = new Date()
	Date enddate = new Date()
	Date signaldate
	boolean finished
	String type
	String description
	long idprocess
	int ord
	User user
	String status
	String initstatus
	Task task
	Queue parent
	Taskstatus taskstatus
	int repeatcount
	int maxrepeat
	String registers
	String parentchat
	
	static constraints = {
		type blank: false, unique: false
		startdate blank: false
		registers size: 1..1000
	}
	
	static hasOne = [idprocess:Process]
	
	/**
	 * Возвращает экземпляр самого верхнего предка для любого экземпляра
	 * @param q
	 * @return
	 */
	public static Queue getTopInstance(Queue q){
		if(q == null){
			return null
		}
		Queue p = q
		while(p.parent != null && p.id != p.parent.id){
			if(p.parent != null){
				p = p.parent
			}
		}
		return p
	}
	
	/**
	 * Возвращает значение регистра из экземпляра процесса или из таска, если экземпляра процесса нет
	 * @param q
	 * @param register
	 * @return
	 */
	public static String getRegisterValue(Queue q, String register){
		q = Queue.getTopInstance(q)
		if(q == null){
			return '?'
		}
		return Utils.getValueFromPair(q.registers, register)
	}
	
	/**
	 * Записывает значение регистра в экземпляра процесса или в таска, если экземпляра процесса нет
	 * @param q
	 * @param params Мэп параметров, пришедших в реквесте от клиента
	 * @return Возвращаем экземпляр верхнего предка для сохранения
	 */
	public static Queue setRegisterValue(Queue q, Map<String, String> params){
		Queue p = Queue.getTopInstance(q)
		if(p == null){
			p = q
		}
		//Разложим текущие значения из поля registers предка в мэп
		Map<String, String> regMap = Utils.splitToMap(p.registers)
		def registers = Register.list()
		// Сохраним только параметры, которые есть регистры
		for(Register r: registers){
			String v = params.get(r.code)
			if(v != null){
				regMap.put(r.code, v)
			}
		}
		// Сохраним строку регистров
		String s = Utils.mapToString(regMap)
		p.registers = s
		return p
	}
}
