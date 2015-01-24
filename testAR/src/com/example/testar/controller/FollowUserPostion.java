package com.example.testar.controller;

import static com.example.testar.services.Constants.JSON_CONTENT;
import static com.example.testar.services.Constants.PARAMETER_LATITUDE;
import static com.example.testar.services.Constants.PARAMETER_LONGITUDE;
import static com.example.testar.services.Constants.PARAMETER_ID;

import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.testar.R;
import com.example.testar.model.DisplayTreeListener;
import com.example.testar.model.Tree;
import com.example.testar.services.TreeServices;
import com.example.testar.utils.ErrorCode;
import com.example.testar.utils.SpinnerDialog;
import com.example.testar.utils.TaskListener;
import com.google.android.gms.maps.model.LatLng;

public class FollowUserPostion {
	private static final int MIN_TIME_INTERVAL_MS = 30000;
	private static final int MIN_DISTANCE_INTERVAL_M = 3;
	private static final int RADIUS = Integer.MAX_VALUE;

	private HashSet<Long> surroundingTrees;
	private boolean isFollowing;
	private SpinnerDialog waitingSpinnerDialog;
	private Activity context;
	private DisplayTreeListener listener;
	private LocationManager locManager;
	private UserPositionListener locListener;

	private TaskListener getClosestTrees = new TaskListener() {

		@Override
		public void onSuccess(String content) {
			waitingSpinnerDialog.dismiss();
			if (isFollowing){
				//parse content
				ArrayList<Long> newTrees = new ArrayList<Long>();
				JSONObject treeList;
				try {
					treeList = new JSONObject(content);

					JSONArray treeArray = treeList.getJSONArray(JSON_CONTENT);
					for(int i=0; i<treeArray.length(); i++){
						JSONObject tree = treeArray.getJSONObject(i);

						long id = Long.parseLong(tree.getString(PARAMETER_ID));					
						Double latitude = Double.parseDouble(tree.getString(PARAMETER_LATITUDE));
						Double longitude = Double.parseDouble(tree.getString(PARAMETER_LONGITUDE));

						//ask listener to display new trees
						newTrees.add(id); 
						if (!surroundingTrees.contains(id)){
							listener.addTree(new Tree(latitude, longitude, id));
							surroundingTrees.add(id);
						}
					}

					//ask listener to remove trees that are not in newTrees list
					surroundingTrees.removeAll(newTrees);
					for (Long idTree : surroundingTrees){
						listener.removeTree(idTree);
					}

					surroundingTrees = new HashSet<Long>(newTrees);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}

		@Override
		public void onFailure(ErrorCode errCode) {
			//Stop waiting dialog
			waitingSpinnerDialog.dismiss();
			if (errCode == ErrorCode.REQUEST_FAILED){
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setMessage(R.string.request_failed);
				alertDialogBuilder.setNeutralButton(R.string.neutral_button, null);
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		}
	};


	public FollowUserPostion(Activity activity){
		surroundingTrees = new HashSet<Long>();
		isFollowing = false;
		context = null;
		listener = null;
		context = activity;

	}

	public void startFollowing(DisplayTreeListener displayTreeListener){
		listener = displayTreeListener;
		if (!isFollowing){
			//Start waiting dialog
			FragmentManager fm = context.getFragmentManager();
			waitingSpinnerDialog = new SpinnerDialog(context.getString(R.string.loading));
			waitingSpinnerDialog.show(fm, "");
			
			isFollowing = true;

			//TODO à tester

			locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			locListener = new UserPositionListener();
			PackageManager pm = context.getPackageManager();
			boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
			if(!hasGps){
				locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_INTERVAL_MS, MIN_DISTANCE_INTERVAL_M, locListener);
			} else if (hasGps && !locManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
				showAlertMessageNoGps();
			} else {
				locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_INTERVAL_MS, MIN_DISTANCE_INTERVAL_M, locListener);
			}


			/* TEST
			Tree tree1 = new Tree(45.778339,4.874631, 0, "Rue de la doua");
			Tree tree2 = new Tree(45.781573, 4.872156, 1, "Gaston Berger");
			Tree tree3 = new Tree(45.781856, 4.870511, 2, "BU");
			Tree tree4 = new Tree(45.781035, 4.873657, 3, "Beurk");

			listener.addTree(tree1);
			listener.addTree(tree2);
			listener.addTree(tree3);
			listener.addTree(tree4); */
		}

	}

	public void stopFollowing(){
		if (isFollowing){
			locManager.removeUpdates(locListener);
		}
	}

	private void showAlertMessageNoGps() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.gps_disabled)
		.setCancelable(false)
		.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		})
		.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_DISTANCE_INTERVAL_M, MIN_DISTANCE_INTERVAL_M, locListener);    
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public class UserPositionListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			TreeServices.getClosestTrees(new LatLng(location.getLatitude(), location.getLongitude()), getClosestTrees, RADIUS);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}


}
