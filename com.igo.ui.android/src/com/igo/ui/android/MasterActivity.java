package com.igo.ui.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MasterActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //getMenuInflater().inflate(R.menu.main, menu);
      return true;
    }
}