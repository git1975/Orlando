package com.igo.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMyyyy");
	private static SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmssZ");
	private static SimpleDateFormat sdfFull = new SimpleDateFormat("ddMMyyyyHHmmssZ");
	
	public static boolean isTimeInInterval(Date dt, Date dt1, Date dt2){
		String currentDate = sdfDate.format(dt);
		String time1 = sdfTime.format(dt1);
		String time2 = sdfTime.format(dt2);
		
		//System.out.println(currentDate + time1 + "|" + currentDate + time2);
		
		try {
			dt1 = sdfFull.parse(currentDate + time1);
			dt2 = sdfFull.parse(currentDate + time2);
			
			//System.out.println(sdfFull.format(dt));
			//System.out.println(sdfFull.format(dt1));
			//System.out.println(sdfFull.format(dt2));
			
			if(dt.after(dt1) && dt.before(dt2)){
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			
			return false;
		}		
	}
}
