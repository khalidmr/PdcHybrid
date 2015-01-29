package com.example.testar.controller;

import static com.example.testar.services.Constants.JSON_CONTENT;
import static com.example.testar.services.Constants.JSON_COORDINATES;
import static com.example.testar.services.Constants.JSON_CROWN;
import static com.example.testar.services.Constants.JSON_DISTANCE;
import static com.example.testar.services.Constants.JSON_HEIGHT;
import static com.example.testar.services.Constants.JSON_KIND;
import static com.example.testar.services.Constants.JSON_NAME;
import static com.example.testar.services.Constants.JSON_SPECY;
import static com.example.testar.services.Constants.JSON_TRUNK;
import static com.example.testar.services.Constants.JSON_TYPE;
import static com.example.testar.services.Constants.PARAMETER_ID;
import static com.example.testar.services.Constants.PARAMETER_LATITUDE;
import static com.example.testar.services.Constants.PARAMETER_LONGITUDE;

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
			try {
				waitingSpinnerDialog.dismiss();
			} catch (Exception e){
				e.printStackTrace();
			}
			if (isFollowing){
				//parse content
				ArrayList<Long> newTrees = new ArrayList<Long>();
				JSONObject treeList;
				try {
					treeList = new JSONObject(content);

					JSONArray treeArray = treeList.getJSONArray(JSON_CONTENT);
					for(int i=0; i<treeArray.length(); i++){
						JSONObject tree = treeArray.getJSONObject(i);

						final long id = Long.parseLong(tree.getString(PARAMETER_ID));	
						JSONObject coordinates = tree.getJSONObject(JSON_COORDINATES);
						final Double latitude = Double.parseDouble(coordinates.getString(PARAMETER_LATITUDE));
						final Double longitude = Double.parseDouble(coordinates.getString(PARAMETER_LONGITUDE));
						Double proximity = Double.parseDouble(tree.getString(JSON_DISTANCE));
						final int distance = proximity.intValue();

						//ask listener to display new trees
						newTrees.add(id); 
						if (!surroundingTrees.contains(id)){
							//get information about new tree
							TreeServices.getTreeInfo(id, new TaskListener() {

								@Override
								public void onSuccess(String content) {

									try {
										JSONObject response = new JSONObject(content);
										JSONObject treeInfo = response.getJSONObject(JSON_CONTENT);

										String name = treeInfo.getString(JSON_NAME);
										String type = treeInfo.getString(JSON_TYPE);
										String specy = treeInfo.getString(JSON_SPECY);
										String trunk = treeInfo.getString(JSON_TRUNK);
										String height = treeInfo.getString(JSON_HEIGHT);
										String crown = treeInfo.getString(JSON_CROWN);
										String kind = treeInfo.getString(JSON_KIND);

										listener.addTree(new Tree(latitude, longitude, id, distance, name, kind, specy, type, trunk, crown, height));

									} catch (JSONException e) {
										e.printStackTrace();
										onFailure(ErrorCode.FAILED);
									}
								}

								@Override
								public void onFailure(ErrorCode errCode) {
									waitingSpinnerDialog.dismiss();
									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
									alertDialogBuilder.setMessage(R.string.request_info_failed);
									alertDialogBuilder.setNeutralButton(R.string.neutral_button, null);
									AlertDialog alertDialog = alertDialogBuilder.create();
									alertDialog.show();
								}
							});


							surroundingTrees.add(id);
						}
					}

					//ask listener to remove trees that are not in newTrees list
					surroundingTrees.removeAll(newTrees);
					for (Long idTree : surroundingTrees){
						listener.removeTree(idTree);
					}

					surroundingTrees = new HashSet<Long>(newTrees);

					try {
						waitingSpinnerDialog.dismiss();
					} catch (Exception e){
						e.printStackTrace();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					onFailure(ErrorCode.FAILED);
				}

			}

		}

		@Override
		public void onFailure(ErrorCode errCode) {
			//Stop waiting dialog
			try {
				waitingSpinnerDialog.dismiss();
			} catch (Exception e){
				e.printStackTrace();
			}
			if (errCode == ErrorCode.REQUEST_FAILED){
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setMessage(R.string.request_failed);
				alertDialogBuilder.setNeutralButton(R.string.neutral_button, null);
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		}
	};

	/**
	 * FollowUserPosition constructor
	 * @param activity Activity which will be notified
	 */
	public FollowUserPostion(Activity activity){
		surroundingTrees = new HashSet<Long>();
		isFollowing = false;
		context = null;
		listener = null;
		context = activity;

	}

	/**
	 * Start to follow user position. This method gets user location and surrounding trees asynchronously
	 * @param displayTreeListener Listener to notify when it is done
	 */
	public void startFollowing(DisplayTreeListener displayTreeListener){
		listener = displayTreeListener;
		if (!isFollowing){
			//Start waiting dialog
			FragmentManager fm = context.getFragmentManager();
			waitingSpinnerDialog = new SpinnerDialog(context.getString(R.string.loading));
			waitingSpinnerDialog.show(fm, "");

			isFollowing = true;
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
		}
	}

	/**
	 * Stop to follow user location. User location will not be updated any more
	 */
	public void stopFollowing(){
		if (isFollowing){
			locManager.removeUpdates(locListener);
		}
	}

	/**
	 * Inform user if GPS is disabled
	 */
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
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

	}


}
