package com.igo.server

import java.text.SimpleDateFormat;

import grails.converters.JSON
import grails.transaction.Transactional

import java.util.Date;
import java.util.regex.Matcher
import java.util.regex.Pattern;

@Transactional
class InitDbService {
	def initDatabase(){
		if(!Role.count) {
			Role role1 = new com.igo.server.Role(name: 'head', description: 'Директор').save(failOnError: true)
			Role role2 = new com.igo.server.Role(name: 'mgr', description: 'Управляющий').save(failOnError: true)
		}
		if(!User.count) {
			Role role1 = Role.find("from Role as a where a.name = ?", ['head'])
			Role role2 = Role.find("from Role as a where a.name = ?", ['mgr'])

			User usr1 = new User(login: 'user1', username: 'Иван Иванов', password: '1', role: role1).save(failOnError: true)
			User usr2 = new User(login: 'user2', username: 'Петр Петров', password: '1', role: role2).save(failOnError: true)
			User usr3 = new User(login: 'user3', username: 'Жорж Милославский', password: '1', role: role2).save(failOnError: true)
		}
		if(Process.findByName('production') == null){
			Process proc = new Process(name: 'production', description: 'производство', repeatevery: 0).save(failOnError: true)
		}
		if(true) {
			Process proc = Process.find("from Process as a where a.name = ?", ['production'])

			User usr1 = User.find("from User as a where a.login = ?", ['user1'])
			User usr2 = User.find("from User as a where a.login = ?", ['user2'])
			User usr3 = User.find("from User as a where a.login = ?", ['user3'])

			SimpleDateFormat sdfTime = new SimpleDateFormat("mm")

			if(Button.findByName('start') == null)new Task(name: 'start', description: 'старт', user: usr1, ord: 1, startdate: sdfTime.parse("00"), signaldate: sdfTime.parse("01"), enddate: sdfTime.parse("01"), process: proc).save(failOnError: true)
			if(Button.findByName('prepare') == null)new Task(name: 'prepare', description: 'подготовка', user: usr2, ord: 2, startdate: sdfTime.parse("01"), signaldate: sdfTime.parse("01"), enddate: sdfTime.parse("02"), process: proc).save(failOnError: true)
			if(Button.findByName('running') == null)new Task(name: 'running', description: 'исполнение', user: usr3, ord: 3, startdate: sdfTime.parse("02"), signaldate: sdfTime.parse("02"), enddate: sdfTime.parse("03"), process: proc).save(failOnError: true)
			if(Button.findByName('finish') == null)new Task(name: 'finish', description: 'завершение', user: usr3, ord: 4, startdate: sdfTime.parse("03"), signaldate: sdfTime.parse("04"), enddate: sdfTime.parse("05"), process: proc).save(failOnError: true)
		}
		if(!Queue.count) {
			//Process proc = Process.findByName("production")
			//Queue queue = new Queue(type: 'StartProcess', finished: false, description: 'description', idprocess: proc.id)
			//.save(failOnError: true)
		}
		if(!Deviation.count) {
			new Deviation(name: 'Отказ').save(failOnError: true)
		}
		if(true) {
			if(Button.findByCode('YES') == null) new Button(code: 'YES', name: 'Да', replystatus: 'REPLY_YES').save(failOnError: true)
			if(Button.findByCode('NO') == null) new Button(code: 'NO', name: 'Нет', replystatus: 'REPLY_NO').save(failOnError: true)
			if(Button.findByCode('FINISH') == null) new Button(code: 'FINISH', name: 'Нет', replystatus: 'REPLY_FINISH').save(failOnError: true)
			if(Button.findByCode('HAND') == null) new Button(code: 'HAND', name: 'Ручной режим', replystatus: 'REPLY_HAND').save(failOnError: true)
			if(Button.findByCode('BTN_1') == null) new Button(code: 'BTN_1', name: 'Хорошо', replystatus: 'REPLY_1').save(failOnError: true)
			if(Button.findByCode('BTN_2') == null) new Button(code: 'BTN_2', name: 'Плохо', replystatus: 'REPLY_2').save(failOnError: true)
		}
		if(!Taskstatus.count) {
			Button btn1 = Button.find("from Button as a where a.code = ?", ['YES'])
			Button btn2 = Button.find("from Button as a where a.code = ?", ['FINISH'])
			Button btn3 = Button.find("from Button as a where a.code = ?", ['BTN_1'])
			Button btn4 = Button.find("from Button as a where a.code = ?", ['BTN_2'])

			Taskstatus ts1, ts2, ts3, ts4

			//start
			Task task1 = Task.find("from Task as a where a.name = ?", ['start'])
			ts1 = new Taskstatus(status: 'INIT', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Начинается цикл <process>, под контролем <user=user1>').save(failOnError: true)
			ts1.task = task1
			//prepare
			task1 = Task.find("from Task as a where a.name = ?", ['prepare'])
			ts1 = new Taskstatus(status: 'INIT', msgtype: 'CMD', sendTo: 'user2', color: 2, msgtext: 'Этап <stage>. Продолжить выполнение?').save(failOnError: true)
			ts1.addToButtons(btn1)
			ts1.addToButtons(btn2)
			ts1.task = task1
			ts2 = new Taskstatus(status: 'REPLY_YES', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Штатный режим этапа <stage>').save(failOnError: true)
			ts2.task = task1
			ts3 = new Taskstatus(status: 'REPLY_FINISH', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Досрочный финиш этапа <stage>').save(failOnError: true)
			ts3.task = task1
			//running
			task1 = Task.find("from Task as a where a.name = ?", ['running'])
			ts1 = new Taskstatus(status: 'INIT', msgtype: 'CMD', sendTo: 'user2', color: 2, msgtext: 'Этап <stage>. Продолжить выполнение?').save(failOnError: true)
			ts1.addToButtons(btn1)
			ts1.addToButtons(btn2)
			ts1.task = task1
			ts2 = new Taskstatus(status: 'REPLY_YES', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Штатный режим этапа <stage>').save(failOnError: true)
			ts2.task = task1
			ts3 = new Taskstatus(status: 'REPLY_FINISH', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Досрочный финиш этапа <stage>').save(failOnError: true)
			ts3.task = task1
			//finish
			task1 = Task.find("from Task as a where a.name = ?", ['finish'])
			ts1 = new Taskstatus(status: 'INIT', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Завершается цикл <process>').save(failOnError: true)
			ts1.task = task1
			ts2 = new Taskstatus(status: 'INIT', msgtype: 'CMD', sendTo: 'user1', color: 2, msgtext: 'Оцените исполнение цикла <process>?').save(failOnError: true)
			ts2.addToButtons(btn3)
			ts2.addToButtons(btn4)
			ts2.task = task1
			ts3 = new Taskstatus(status: 'REPLY_1', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Оценка цикла <process> Хорошо').save(failOnError: true)
			ts3.task = task1
			ts4 = new Taskstatus(status: 'REPLY_2', msgtype: 'INFO', sendTo: 'all', color: 1, msgtext: 'Оценка цикла <process> Плохо').save(failOnError: true)
			ts4.task = task1
		}
	}

	def initDatabaseDemo(){
		if(!Accessgroup.count) {
			new Accessgroup(code: 'work', name: 'Работа', description: 'Работа').save(failOnError: true)
			new Accessgroup(code: 'home', name: 'Дом', description: 'Дом').save(failOnError: true)
		}
		if(!Role.count) {
			new Role(code: 'head', name: 'Директор', description: 'Директор').save(failOnError: true)
			new Role(code: 'mgr', name: 'Управляющий', description: 'Управляющий').save(failOnError: true)
		}
		if(!User.count) {
			new User(login: 'user1', username: 'Иван Иванов', password: '1',
			role: Role.findByCode('head'), accessgroup: Accessgroup.findByCode('work')).save(failOnError: true)
			new User(login: 'user2', username: 'Петр Петров', password: '1',
			role: Role.findByCode('mgr'), accessgroup: Accessgroup.findByCode('work')).save(failOnError: true)
			new User(login: 'user3', username: 'Владимир Владимиров', password: '1',
			role: Role.findByCode('mgr'), accessgroup: Accessgroup.findByCode('work')).save(failOnError: true)
		}
		if(!Process.count) {
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss")

			new Process(name: 'demo', description: 'запрос в команде demo', active: 1, repeatevery: 0,
			startdate: sdfTime.parse("20150801T06:00:00"), accessgroup: Accessgroup.findByCode('work')).save(failOnError: true)
			new Process(name: 'demo_ibr', description: 'запрос клиенту demo', active: 1, repeatevery: 0, accessgroup: Accessgroup.findByCode('work')).save(failOnError: true)
		}
		if(!Task.count) {
			SimpleDateFormat sdfTime = new SimpleDateFormat("mm")

			new Task(name: 'notify', description: 'Информирование', ord: 1, process: Process.findByName('demo'),
			startdate: sdfTime.parse("00"), signaldate: sdfTime.parse("00"), enddate: sdfTime.parse("01")).save(failOnError: true)
			new Task(name: 'confirm', description: 'Контроль', ord: 2, process: Process.findByName('demo'),
			startdate: sdfTime.parse("01"), signaldate: sdfTime.parse("01"), enddate: sdfTime.parse("05")).save(failOnError: true)
			new Task(name: 'finish', description: 'Отчет', ord: 3,  process: Process.findByName('demo'),
			startdate: sdfTime.parse("05"), signaldate: sdfTime.parse("05"), enddate: sdfTime.parse("06")).save(failOnError: true)

			new Task(name: 'request', description: 'Запрос', ord: 1,  process: Process.findByName('demo_ibr'),
			startdate: sdfTime.parse("00"), signaldate: sdfTime.parse("00"), enddate: sdfTime.parse("01")).save(failOnError: true)
		}
		if(!Register.count) {
			new Register(code: 'time', name: 'Время', description: 'Время').save(failOnError: true)
		}
		if(!Button.count) {
			new Button(code: 'YES', name: 'Да', replystatus: 'REPLY_YES').save(failOnError: true)
			new Button(code: 'NO', name: 'Нет', replystatus: 'REPLY_NO').save(failOnError: true)
			new Button(code: 'OK', name: 'Подтверждаю', replystatus: 'OK').save(failOnError: true)
			new Button(code: 'POSTPONE', name: 'Перенос', replystatus: 'POSTPONE', register: Register.findByCode('time')).save(failOnError: true)
		}
		if(!Taskstatus.count) {
			new Taskstatus(status: 'INIT', msgtype: 'INFO', sendTo: 'all', color: 1, lifetime: 1, 
				msgtext: 'Начинается процедура <process>', task: Task.findByName('notify')).save(failOnError: true)
			new Taskstatus(status: 'INIT', msgtype: 'INFO', sendTo: 'user1', color: 1, lifetime: 1, 
				msgtext: '<user=user1>, ожидаем ответа от <user=user2> на запрос о начале работы цеха сегодня в 9:00. Время на ответ 1 минута', task: Task.findByName('confirm')).save(failOnError: true)
			Taskstatus ts1 = new Taskstatus(status: 'INIT', msgtype: 'CMD', sendTo: 'user2', color: 1, lifetime: 1,
				msgtext: '<user=user2>, подтверди полную готовность начала работы цеха с <reg=time>. Прошу ответить в течении 1 минуты', task: Task.findByName('confirm'), 
				registers: 'time=9:00;', maxrepeat: 3, repeatevery: 1).save(failOnError: true)
			new Taskstatus(status: 'OK', msgtype: 'INFO', sendTo: 'all', color: 1, lifetime: 1,
				msgtext: '<user=user2> подтвердил готовность цеха к <reg=time>', task: Task.findByName('confirm')).save(failOnError: true)
			new Taskstatus(status: 'POSTPONE', msgtype: 'INFO', sendTo: 'all', color: 1, lifetime: 1,
				msgtext: '<user=user2> перенес готовность цеха на <reg=time>', task: Task.findByName('confirm')).save(failOnError: true)
			Taskstatus ts2 = new Taskstatus(status: 'OK', msgtype: 'CMD', sendTo: 'user1', color: 1, lifetime: 1,
				msgtext: 'Подтверди получение сообщения о готовности от <user=user2>', task: Task.findByName('confirm'), maxrepeat: 3, repeatevery: 1).save(failOnError: true)
			new Taskstatus(status: 'END_TASK', msgtype: 'REPORT', sendTo: 'user1', color: 1, lifetime: 1,
				msgtext: 'Итоги. Производство начинает работу в <reg=time>. Взято на контроль. Бонус <user=user2>=<bonus=user2>', task: Task.findByName('confirm')).save(failOnError: true)
			new Taskstatus(status: 'INIT', msgtype: 'INFO', sendTo: 'all', color: 1, lifetime: 1,
				msgtext: 'Демонстрация завершена', task: Task.findByName('finish')).save(failOnError: true)				
	
			ts1.addToButtons(Button.findByCode('OK'))
			ts1.addToButtons(Button.findByCode('POSTPONE'))
			ts2.addToButtons(Button.findByCode('YES'))
			ts2.addToButtons(Button.findByCode('NO'))
			
			new Taskstatus(status: 'INIT', msgtype: 'INFO', sendTo: 'all', color: 1, lifetime: 1,
				msgtext: 'запрос клиенту demo', task: Task.findByName('request')).save(failOnError: true)
		}
	}
}
