package com.igo.ui.android;

import java.util.HashMap;

import android.app.Application;

public class MainApplication extends Application implements DataStorage{
	private HashMap<String, Object> data = new HashMap<String, Object>(1);
	
	public Object getData(String name){
		return data.get(name);
	}
	
	public void setData(String name, Object value){
		data.put(name, value);
	}
}
