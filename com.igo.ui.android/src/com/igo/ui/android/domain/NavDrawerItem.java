package com.igo.ui.android.domain;

public class NavDrawerItem {
	public static final int TYPE_PROCESS = 1;
	public static final int TYPE_PERSONAL = 2;
	public static final int TYPE_CHILD = 3;
    
    private String title;
    private int type;
    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;
     
    public NavDrawerItem(){}
 
    public NavDrawerItem(String title, int type){
        this.title = title;
        this.type = type;
    }
     
    public NavDrawerItem(String title, int type, boolean isCounterVisible, String count){
        this.title = title;
        this.type = type;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }
     
    public String getTitle(){
        return this.title;
    }
     
    public String getCount(){
        return this.count;
    }
     
    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }
     
    public void setTitle(String title){
        this.title = title;
    }
     
    public void setCount(String count){
        this.count = count;
    }
     
    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}