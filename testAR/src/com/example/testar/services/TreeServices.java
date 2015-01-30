package com.example.testar.services;

import static com.example.testar.services.Constants.SERVICE_PROXIMITY;
import static com.example.testar.services.Constants.SERVICE_DETAIL;
import static com.example.testar.services.Constants.SERVICE_NEW;
import static com.example.testar.services.Constants.SERVICE_GET;
import static com.example.testar.services.Constants.URL_BASE;
import static com.example.testar.services.Constants.URL_MESSAGE;
import static com.example.testar.services.Constants.URL_TREE;
import static com.example.testar.services.Constants.PARAMETER_LATITUDE;
import static com.example.testar.services.Constants.PARAMETER_LONGITUDE;
import static com.example.testar.services.Constants.PARAMETER_TREE_ID;
import static com.example.testar.services.Constants.PARAMETER_RADIUS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.testar.utils.RequestExecutor;
import com.example.testar.utils.TaskListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TreeServices {
	static Gson gson = new Gson();
	
	/**
	 * Get surrounding trees asynchronously
	 * @param coordinates User location
	 * @param listener Listener to notify when it is done
	 * @param radius radius within which retrieve trees
	 */
	public static void getClosestTrees(LatLng coordinates, TaskListener listener, int radius)
	{
		//build url
		String url = URL_BASE + URL_TREE + SERVICE_PROXIMITY;

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(PARAMETER_LATITUDE, String.valueOf(coordinates.latitude));
		jsonObject.addProperty(PARAMETER_LONGITUDE, String.valueOf(coordinates.longitude));
		jsonObject.addProperty(PARAMETER_RADIUS, String.valueOf(radius));
		
		String jsonParams = gson.toJson(jsonObject);
		
		RequestExecutor requestExecutor = new RequestExecutor(jsonParams, url, RequestExecutor.RequestType.POST, listener);
		requestExecutor.execute();
	}
	
	/**
	 * Get information about a tree
	 * @param treeId Id of the tree
	 * @param listener Listener to notify when it is done
	 */
	public static void getTreeInfo(long treeId, TaskListener listener)
	{
		//build url
		String url = URL_BASE + URL_TREE + SERVICE_DETAIL;

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(PARAMETER_TREE_ID, String.valueOf(treeId));
		
		String jsonParams = gson.toJson(jsonObject);
		
		RequestExecutor requestExecutor = new RequestExecutor(jsonParams, url, RequestExecutor.RequestType.POST, listener);
		requestExecutor.execute();
	}
	
}
