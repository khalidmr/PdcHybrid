package com.example.testar;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity{

	Button Test1;
	Button Test2;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 Test1=(Button)findViewById(R.id.myTest1);
		 Test2=(Button)findViewById(R.id.myTest2);
		
		Test1.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent myIntentTest1 = new Intent(MainActivity.this,Test1_activity.class);
				startActivity(myIntentTest1);
			}

		});
		
		Test2.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent myIntentTest2 = new Intent(MainActivity.this,Test2_activity.class);
				startActivity(myIntentTest2);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
