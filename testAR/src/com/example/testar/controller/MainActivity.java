package com.example.testar.controller;


import gl.GL1Renderer;
import gl.GLFactory;
import system.ArActivity;
import system.DefaultARSetup;
import worldData.World;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.example.testar.R;
import com.example.testar.model.ArWorld;

public class MainActivity extends ActionBarActivity{
	private static final int RESULT_AR_ACTIVITY = 0;
	Context context;
	Button mapButton;
	Button arButton;
	FollowUserPostion followUserPostion;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		followUserPostion = new FollowUserPostion(this);
		
		mapButton=(Button)findViewById(R.id.map_button);
		arButton=(Button)findViewById(R.id.ar_button);

		mapButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent myIntentTest1 = new Intent(MainActivity.this,MapActivity.class);
				startActivity(myIntentTest1);
			}

		});

		arButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				ArActivity.startWithSetupForResult(MainActivity.this, new DefaultARSetup() {

					@Override
					public void addObjectsTo(GL1Renderer renderer, final World world,
							GLFactory objectFactory) {

						ArWorld arWorld = new ArWorld(context, world, camera);
						followUserPostion.startFollowing(arWorld);
					}
				}, RESULT_AR_ACTIVITY);

			}

		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == RESULT_AR_ACTIVITY) {
	    	followUserPostion.stopFollowing();
	    }
	    
	    super.onActivityResult(requestCode, resultCode, data);

	}
}
