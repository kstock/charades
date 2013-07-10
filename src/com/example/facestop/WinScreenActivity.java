package com.example.facestop;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class WinScreenActivity extends Activity {
	private String scoreStr = "";
	private List<String> passList;
	private List<String> correctList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_screen);
		
		scoreStr += "GOT:\n";
		correctList = (ArrayList<String>) getIntent().getSerializableExtra("correctList");
		if (correctList != null) {
			for (int i = 0; i < correctList.size(); i++){
				scoreStr += correctList.get(i) + "\n";
			}
			
		} else {
			scoreStr = "You suck";
		} // default i guess

		scoreStr += "\nMissed:\n";

		passList = (ArrayList<String>) getIntent().getSerializableExtra("passList");
		if (passList != null) {
			for (int i = 0; i < passList.size(); i++){
				scoreStr += passList.get(i) + "\n";
			}
			
		} else {
			scoreStr = "wow you are master";
		} // default i guess

		

		
		//AutoResizeTextView score = (AutoResizeTextView) findViewById(R.id.score);
		TextView score = (TextView) findViewById(R.id.score);
		score.setText(scoreStr);

	}
}
