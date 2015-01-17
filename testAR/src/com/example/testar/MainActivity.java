package com.example.testar;


import geo.GeoObj;
import gl.Color;
import gl.GL1Renderer;
import gl.GLFactory;
import gl.animations.AnimationFaceToCamera;
import gl.scenegraph.MeshComponent;
import system.ArActivity;
import system.DefaultARSetup;
import util.IO;
import util.Vec;
import worldData.World;

import commands.Command;
import commands.ui.CommandInUiThread;
import commands.ui.CommandShowInfoScreen;
import commands.ui.CommandShowToast;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity{
	Context context;
	Button mapButton;
	Button arButton;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;

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
				//				Intent myIntentTest2 = new Intent(MainActivity.this,TreesARActivity.class);
				//				startActivity(myIntentTest2);

				ArActivity.startWithSetup(MainActivity.this, new DefaultARSetup() {

					@Override
					public void addObjectsTo(GL1Renderer renderer, final World world,
							GLFactory objectFactory) {

						final GeoObj o = new GeoObj(45.77823,4.875113, 170);
//						o.setComp(objectFactory.newArrow());
//						o.setOnClickCommand(new CommandShowToast(context, "Click"));
//						world.add(o);
						
						//show a tree picture at tree location
						ImageView img = new ImageView(context);
						img.setImageResource(R.drawable.arbre);
						MeshComponent mesh = GLFactory.getInstance().newTexturedSquare(
								"arbre", IO.loadBitmapFromView(img));

						mesh.addChild(new AnimationFaceToCamera(camera, 0.5f));
						mesh.setScale(new Vec(5, 5, 5));

						final GeoObj tree = new GeoObj(o, mesh);
						
						mesh.setOnClickCommand(new CommandInUiThread() {
							
							@Override
							public void executeInUiThread() {
								//show overlay
								LayoutInflater inflater = LayoutInflater.from(context); // 1
								View v = inflater.inflate(R.layout.tree_overlay, null);
								MeshComponent meshOverlay = GLFactory.getInstance().newTexturedSquare(
										"overlay", IO.loadBitmapFromView(v));
								
								meshOverlay.addChild(new AnimationFaceToCamera(camera, 0.5f));
								meshOverlay.setScale(new Vec(5, 5, 5));
								
								final GeoObj overlay = new GeoObj(o, meshOverlay);
								meshOverlay.setOnClickCommand(new Command() {
									
									@Override
									public boolean execute() {
										//remove overlay
										world.remove(overlay);
										world.add(tree);
										return false;
									}
								});
								world.remove(tree);
								world.add(overlay);
								}
						});

						world.add(tree);
						
					}
				});

			}

		});
	}

	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		// Inflate the menu; this adds items to the action bar if it is present.
	//		getMenuInflater().inflate(R.menu.main, menu);
	//		return true;
	//	}
	//
	//	@Override
	//	public boolean onOptionsItemSelected(MenuItem item) {
	//		// Handle action bar item clicks here. The action bar will
	//		// automatically handle clicks on the Home/Up button, so long
	//		// as you specify a parent activity in AndroidManifest.xml.
	//		int id = item.getItemId();
	//		if (id == R.id.action_settings) {
	//			return true;
	//		}
	//		return super.onOptionsItemSelected(item);
	//	}
}
