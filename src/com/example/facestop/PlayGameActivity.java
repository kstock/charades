package com.example.facestop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PlayGameActivity extends Activity  implements SensorEventListener {

	private float mLastX, mLastY, mLastZ;

	private boolean mInitialized;

	private SensorManager mSensorManager;
	private final int FLIPCONSTANT = 8;
	
	
	private String wordInPlay;
	private LinkedList<String> wordList;
	private LinkedList<String> passList;
	private LinkedList<String> correctList;

	private Sensor mAccelerometer;

	private final float NOISE = (float) 1.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	String fileName;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fileName = extras.getString("version");
        } else {fileName = "movies.txt";} //default i guess
        
        
        wordList = new LinkedList<String>();
        passList = new LinkedList<String>();
        correctList = new LinkedList<String>();
        
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        
        AssetManager am = getBaseContext().getAssets();
        try {
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	        					am.open(fileName))); 
	        String line;
	        while ((line = in.readLine()) != null){
	        	wordList.add(line);
	        }
	        
	        Collections.shuffle(wordList);
	        wordInPlay = wordList.poll();
	    	AutoResizeTextView currentWord = (AutoResizeTextView) findViewById(R.id.currentWord);
			currentWord.setText(wordInPlay);

	        
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        
        final Handler handler = new Handler();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
            		    Intent intent = new Intent(getBaseContext(), WinScreenActivity.class);
            		    intent.putExtra("passList",passList);
            		    intent.putExtra("correctList",correctList);
            		    startActivity(intent); 
                    }
                });
            }
        },60000);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
	AutoResizeTextView currentWord = (AutoResizeTextView) findViewById(R.id.currentWord);
	currentWord.resizeText();
	float x = event.values[0];
	float y = event.values[1];
	float z = event.values[2];
	
	if (!mInitialized) {
		mLastX = x;
		mLastY = y;
		mLastZ = z;

		mInitialized = true;
	} else {
		float deltaX = Math.abs(mLastX - x);
		float deltaY = Math.abs(mLastY - y);
		float deltaZ = Math.abs(mLastZ - z);
		
		System.out.println("deltaX ==" + deltaX);
		System.out.println("deltaY ==" + deltaY);
		System.out.println("deltaZ ==" + deltaZ);

		if (deltaX < NOISE) deltaX = (float)0.0;
		if (deltaY < NOISE) deltaY = (float)0.0;
		if (deltaZ < NOISE && deltaZ > -1 * NOISE) deltaZ = (float)0.0;
		
		mLastX = x;
		mLastY = y;
		mLastZ = z;
		String word;
		if (wordList.isEmpty()){
			currentWord.setText("exhausted word list");

		}
		else if (z > FLIPCONSTANT && deltaZ > 0) { //pass 
			passList.add(wordInPlay);
			wordInPlay = wordList.poll();
			currentWord.setText(wordInPlay);

		} else if (z < -1 * FLIPCONSTANT && deltaZ > 0) {//got word
			correctList.add(wordInPlay);
			wordInPlay = wordList.poll();
			currentWord.setText(wordInPlay);
		}
		
    	}
	
	}
	
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		}
	
		protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		}
		

	    /**
	     * A call-back for when the user presses the back button.
	     */
	    OnClickListener mBackListener = new OnClickListener() {
	        public void onClick(View v) {
	            finish();
	        }
	    };

	
	
	

}
