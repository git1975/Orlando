import java.text.SimpleDateFormat;

import com.igo.server.Button
import com.igo.server.Deviation
import com.igo.server.InitDbService;
import com.igo.server.Role
import com.igo.server.Task
import com.igo.server.Taskstatus
import com.igo.server.User
import com.igo.server.Process
import com.igo.server.Queue

class BootStrap {
	def initDbService
	
	def init = { 
		servletContext ->
		
		//TimeZone.setDefault(TimeZone.getTimeZone ("GMT+03:00"));
		SimpleDateFormat sdfFull = new SimpleDateFormat("ddMMyyyyHHmmssZ");
		log.debug("now=" + sdfFull.format(new Date()))
		log.debug("System.file.encoding=" + System.getProperty("file.encoding"))
		
		initDbService.initDatabase()
	}
	def destroy = {
	}
}
