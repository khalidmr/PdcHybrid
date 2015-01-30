package com.example.testar.model;

import static com.example.testar.services.Constants.MEASURE_UNIT;
import static com.example.testar.services.Constants.MEASURE_UNIT_TRUNK;
import static com.example.testar.services.Constants.PARAMETER_TREE_ID;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.testar.R;
import com.example.testar.R.drawable;
import com.example.testar.R.id;
import com.example.testar.R.layout;
import com.example.testar.controller.TreeActivity;

import geo.GeoObj;
import gl.GLCamera;
import gl.GLFactory;
import gl.animations.AnimationFaceToCamera;
import gl.scenegraph.MeshComponent;
import util.IO;
import util.Vec;
import worldData.World;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import commands.Command;
import commands.ui.CommandInUiThread;

public class ArWorld implements DisplayTreeListener{
	private Context context;
	private World world;
	private GLCamera camera;
	private LongSparseArray<GeoObj> visibleTrees;

	public ArWorld(Context context, World world, GLCamera glcamera) {
		this.context = context;
		this.world = world;
		this.camera = glcamera;
		this.visibleTrees = new LongSparseArray<GeoObj>();
	}

	@Override
	public void addTree(final Tree tree) {
		final GeoObj o = new GeoObj(tree.getLatitude(),tree.getLongitude());
		Toast.makeText(context, R.string.tree_showing, Toast.LENGTH_SHORT).show();

		//show a tree picture at tree location
		ImageView img = new ImageView(context);
		img.setImageResource(R.drawable.arbre);
		MeshComponent mesh = GLFactory.getInstance().newTexturedSquare(
				"arbre", IO.loadBitmapFromView(img));

		mesh.addChild(new AnimationFaceToCamera(camera, 0.5f));
		mesh.setScale(new Vec(5, 5, 5));

		final GeoObj geoObj = new GeoObj(o, mesh);
		
		mesh.setOnClickCommand(new CommandInUiThread() {
			
			@Override
			public void executeInUiThread() {
				//show overlay
				LayoutInflater inflater = LayoutInflater.from(context);
				View v = inflater.inflate(R.layout.tree_overlay, null);

				TextView height = (TextView) v.findViewById(R.id.tree_height);
				TextView id = (TextView) v.findViewById(R.id.tree_id);
				TextView title = (TextView) v.findViewById(R.id.tv_tree_title);
				TextView distance = (TextView) v.findViewById(R.id.tv_tree_distance);
				TextView crow = (TextView) v.findViewById(R.id.tree_crown);
				TextView trunk = (TextView) v.findViewById(R.id.tree_trunk);
				TextView type = (TextView) v.findViewById(R.id.tree_type);
				TextView kind = (TextView) v.findViewById(R.id.tree_kind);
				TextView specy = (TextView) v.findViewById(R.id.tree_specy);
				
				title.setText(tree.getName());
				distance.setText(tree.getProximity() + MEASURE_UNIT);
				height.setText(tree.getHeight() + MEASURE_UNIT);
				trunk.setText(tree.getTrunk() + MEASURE_UNIT_TRUNK);
				crow.setText(tree.getCrown() + MEASURE_UNIT);
				id.setText(String.valueOf(tree.getId()));
				type.setText(tree.getType());
				kind.setText(tree.getKind());
				specy.setText(tree.getSpecy());	
				
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
						world.add(geoObj);
						visibleTrees.put(tree.getId(), geoObj);
						Intent intent = new Intent(context, TreeActivity.class);
						intent.putExtra(PARAMETER_TREE_ID, tree.getId());
						context.startActivity(intent);	
						return false;
					}
				});
				world.remove(geoObj);
				world.add(overlay);
				visibleTrees.put(tree.getId(), overlay);
			}
		});

		world.add(geoObj);
		visibleTrees.put(tree.getId(), geoObj);
		
	}

	@Override
	public void removeTree(long treeId) {
		GeoObj geoObj = visibleTrees.get(treeId);
		world.remove(geoObj);
		visibleTrees.remove(treeId);
		
	}


}
