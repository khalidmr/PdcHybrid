package com.example.testar.services;

import static com.example.testar.services.Constants.SERVICE_PROXIMITY;
import static com.example.testar.services.Constants.URL_BASE;
import static com.example.testar.services.Constants.URL_TREE;
import static com.example.testar.services.Constants.PARAMETER_LATITUDE;
import static com.example.testar.services.Constants.PARAMETER_LONGITUDE;
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

	
	/* Snippet to use before calling get
	 * 		
		if (paramsNameValue != null)
	    {
	    	url+="/?";
	    	for(int i=0; i<paramsNameValue.size(); i++)
	    	{
	    		url+=paramsNameValue.get(i).getName()+"="+paramsNameValue.get(i).getValue();
	    		if(i<paramsNameValue.size()-1)
	    		{
	    			url+="&";
	    		}
	    	}
	    }
	 */
	
/*	private static JsonArray createJsonArrayCoordinates(List<LatLng> coordinates)
	{
		JsonArray coordinatesJson = new JsonArray();
		for(int i=0 ; i<coordinates.size() ; i++)
		{
			LatLng point = coordinates.get(i);
			JsonObject jsonPoint = new JsonObject();
			jsonPoint.addProperty(PARAMETER_LATITUDE, String.valueOf(point.latitude));
			jsonPoint.addProperty(PARAMETER_LONGITUDE, String.valueOf(point.longitude));
			coordinatesJson.add(jsonPoint);
		}
		return coordinatesJson;
	}*/

}
