package com.igo.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {
	public static SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMyyyy");
	public static SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmssZ");
	public static SimpleDateFormat sdfTime2 = new SimpleDateFormat("HHmmss");
	public static SimpleDateFormat sdfFull = new SimpleDateFormat("ddMMyyyyHHmmssZ");
	
	public static boolean isTimeInInterval(Date dt, Date dt1, Date dt2){
		String currentDate = sdfDate.format(dt);
		String time1 = sdfTime.format(dt1);
		String time2 = sdfTime.format(dt2);
		
		//System.out.println(currentDate + " " + time1 + "|" + currentDate + " " + time2);
		
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
	
	public static boolean isNowTimeInInterval(Date dt1, Date dt2){
		String s = sdfTime.format(new Date());
		try {
			return isTimeInInterval(sdfTime.parse(s), dt1, dt2);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static long dateMinutesInterval(Date dt1, Date dt2){
		Date diff = new Date(dt2.getTime() - dt1.getTime());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(diff);
		int minutes = calendar.get(Calendar.MINUTE);
		
		long result = minutes;
		
		return result;
	}
	
	public static Date addMinutes(Date target, Date minutes){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(minutes);
		int m = calendar.get(Calendar.MINUTE);
		
		calendar.setTime(target);
		calendar.add(Calendar.MINUTE, m);
		
		return calendar.getTime();
	}
	
	public static List reverse(List list){
		if(list == null){
			return null;
		}
		List result = new ArrayList(list.size());
		for(int i = list.size() - 1; i >= 0; i--){
			result.add(list.get(i));
		}
		
		return result;
	}
}
