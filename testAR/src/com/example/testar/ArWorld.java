package com.example.testar;

import geo.GeoObj;
import gl.GLFactory;
import gl.animations.AnimationFaceToCamera;
import gl.scenegraph.MeshComponent;
import util.IO;
import util.Vec;
import worldData.World;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import commands.Command;
import commands.ui.CommandInUiThread;

public class ArWorld implements DisplayTreeListener{
	private Context context;
	private World world;

	public ArWorld(Context context, World world) {
		this.context = context;
		this.world = world;
	}

	@Override
	public void addTree(final Tree tree) {
		final GeoObj o = new GeoObj(tree.getLatitude(),tree.getLongitude());

		//show a tree picture at tree location
		ImageView img = new ImageView(context);
		img.setImageResource(R.drawable.arbre);
		MeshComponent mesh = GLFactory.getInstance().newTexturedSquare(
				"arbre", IO.loadBitmapFromView(img));

		//mesh.addChild(new AnimationFaceToCamera(camera, 0.5f));
		mesh.setScale(new Vec(5, 5, 5));

		final GeoObj geoObj = new GeoObj(o, mesh);
		
		mesh.setOnClickCommand(new CommandInUiThread() {
			
			@Override
			public void executeInUiThread() {
				//show overlay
				LayoutInflater inflater = LayoutInflater.from(context);
				View v = inflater.inflate(R.layout.tree_overlay, null);
				TextView id = (TextView) v.findViewById(R.id.tree_id);
				TextView descr = (TextView) v.findViewById(R.id.tree_descr);
				id.setText(String.valueOf(tree.getId()));
				descr.setText(String.valueOf(tree.getDescription()));
				
				MeshComponent meshOverlay = GLFactory.getInstance().newTexturedSquare(
						"overlay", IO.loadBitmapFromView(v));
				
				//meshOverlay.addChild(new AnimationFaceToCamera(camera, 0.5f));
				meshOverlay.setScale(new Vec(5, 5, 5));
				
				final GeoObj overlay = new GeoObj(o, meshOverlay);
				meshOverlay.setOnClickCommand(new Command() {
					
					@Override
					public boolean execute() {
						//remove overlay
						world.remove(overlay);
						world.add(geoObj);
						return false;
					}
				});
				world.remove(geoObj);
				world.add(overlay);
				}
		});

		world.add(geoObj);
		
	}

	@Override
	public void removeTree(Tree tree) {
		// TODO Auto-generated method stub
		
	}

}
