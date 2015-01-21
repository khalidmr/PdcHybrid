package com.example.testar;


import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.util.LongSparseArray;
import android.support.v7.app.ActionBarActivity;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MapActivity extends ActionBarActivity implements DisplayTreeListener{
	private static final int ZOOM_LEVEL = 15;
	
	private com.google.android.gms.maps.GoogleMap googleMap = null;
	private LongSparseArray<Marker> visibleMarkers = new LongSparseArray<Marker>();
	private HashMap<String, Tree> markerData = new HashMap<String, Tree>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		googleMap = fm.getMap();
		googleMap.setMyLocationEnabled(true);
		
		FollowUserPostion.startFollowing(this);
		
		googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
			
			@Override
			public View getInfoWindow(Marker arg0) {
				return null;
			}
			
			@Override
			public View getInfoContents(Marker marker) {
				View v = getLayoutInflater().inflate(R.layout.tree_overlay, null);
				TextView id = (TextView) v.findViewById(R.id.tree_id);
				TextView desc = (TextView) v.findViewById(R.id.tree_descr);
				TextView msg = (TextView) v.findViewById(R.id.tree_lastmsg);
				
				Tree tree = markerData.get(marker.getId());
				id.setText(String.valueOf(tree.getId()));
				desc.setText(String.valueOf(tree.getDescription()));
				ArrayList<String> messages = tree.getMessages();
				msg.setText(messages.get(messages.size()-1));

				return v;
			}
		});
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
