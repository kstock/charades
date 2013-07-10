package com.example.facestop;

import java.io.IOException;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class MainActivity extends Activity {
	
	private final String PATH = "cards";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu2);
        Resources res = getResources(); //if you are in an activity
        AssetManager am = res.getAssets();
        String[] fileList; 
		try {
			fileList = am.list(PATH);
		} catch (IOException e) {
			e.printStackTrace();
			// TODO do a toast of something?
			fileList = new String[]{"error"};
		}

        for ( int i = 0;i<fileList.length;i++)
            {
        		addButton(fileList[i]);
            }
    } 
    public void addButton(final String name){
        LinearLayout ll = (LinearLayout)findViewById(R.id.linearLayout);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	
    	Button button = new Button(this);
    	button.setText(name.substring(0, name.length()-4)); //remove ".txt"
    	button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Intent intent = new Intent(getBaseContext(),
            		    							PlayGameActivity.class);
		    intent.putExtra("version", PATH + "/" + name);
		    startActivity(intent); 
		    }
        }); 
    	
    	ll.addView(button,lp);
    	
    } //end addButton
}//end MainActivity