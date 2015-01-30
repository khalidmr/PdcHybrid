package com.example.testar.controller;

import static com.example.testar.services.Constants.MEASURE_UNIT;
import static com.example.testar.services.Constants.MEASURE_UNIT_TRUNK;
import static com.example.testar.services.Constants.PARAMETER_TREE_ID;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.example.testar.R;
import com.example.testar.model.DisplayTreeListener;
import com.example.testar.model.Tree;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends ActionBarActivity implements DisplayTreeListener{
	private static final int ZOOM_LEVEL = 15;
	private static final LatLng TETE_DOR_POSITION = new LatLng(45.77853,4.854176);
	
	private com.google.android.gms.maps.GoogleMap googleMap = null;
	private LongSparseArray<Marker> visibleMarkers = new LongSparseArray<Marker>();
	private HashMap<String, Tree> markerData = new HashMap<String, Tree>();
	private FollowUserPostion followUserPostion;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		followUserPostion = new FollowUserPostion(this);
		context = this;
		
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		googleMap = fm.getMap();
		googleMap.setMyLocationEnabled(true);	
		
		CameraPosition cameraPosition = CameraPosition.fromLatLngZoom(TETE_DOR_POSITION, ZOOM_LEVEL) ;
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				Tree tree = markerData.get(marker.getId());
				Intent intent = new Intent(context, TreeActivity.class);
				intent.putExtra(PARAMETER_TREE_ID, tree.getId());
				startActivity(intent);			
			}
		});
		googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
			
			@Override
			public View getInfoWindow(Marker marker) {
				return null;
			}
			
			@Override
			public View getInfoContents(Marker marker) {
				View v = getLayoutInflater().inflate(R.layout.tree_overlay, null);
				
				TextView height = (TextView) v.findViewById(R.id.tree_height);
				TextView id = (TextView) v.findViewById(R.id.tree_id);
				TextView title = (TextView) v.findViewById(R.id.tv_tree_title);
				TextView distance = (TextView) v.findViewById(R.id.tv_tree_distance);
				TextView crow = (TextView) v.findViewById(R.id.tree_crown);
				TextView trunk = (TextView) v.findViewById(R.id.tree_trunk);
				TextView type = (TextView) v.findViewById(R.id.tree_type);
				TextView kind = (TextView) v.findViewById(R.id.tree_kind);
				TextView specy = (TextView) v.findViewById(R.id.tree_specy);
				
				Tree tree = markerData.get(marker.getId());
				
				title.setText(tree.getName());
				distance.setText(tree.getProximity() + MEASURE_UNIT);
				height.setText(tree.getHeight() + MEASURE_UNIT);
				trunk.setText(tree.getTrunk() + MEASURE_UNIT_TRUNK);
				crow.setText(tree.getCrown() + MEASURE_UNIT);
				id.setText(String.valueOf(tree.getId()));
				type.setText(tree.getType());
				kind.setText(tree.getKind());
				specy.setText(tree.getSpecy());	
				
				return v;
			}
		});
			
		
		followUserPostion.startFollowing(this);
	}

	@Override
	public void addTree(Tree tree) {
		Marker marker = googleMap.addMarker(new MarkerOptions()
	     .position(new LatLng(tree.getLatitude(), tree.getLongitude()))
	     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		
		visibleMarkers.put(tree.getId(), marker);
		markerData.put(marker.getId(), tree);

	}

	@Override
	public void removeTree(long treeId) {
		Marker markerToRemove = visibleMarkers.get(treeId);
		
		markerData.remove(markerToRemove.getId());
		markerToRemove.remove();
		visibleMarkers.remove(treeId);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		followUserPostion.stopFollowing();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.test1_activity, menu);
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
