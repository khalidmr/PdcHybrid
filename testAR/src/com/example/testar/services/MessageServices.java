package com.example.testar.services;

import static com.example.testar.services.Constants.PARAMETER_TREE_ID;
import static com.example.testar.services.Constants.SERVICE_GET;
import static com.example.testar.services.Constants.SERVICE_NEW;
import static com.example.testar.services.Constants.URL_BASE;
import static com.example.testar.services.Constants.URL_MESSAGE;
import static com.example.testar.services.Constants.JSON_SENDER;
import static com.example.testar.services.Constants.JSON_CONTENT;

import com.example.testar.model.Message;
import com.example.testar.utils.RequestExecutor;
import com.example.testar.utils.TaskListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MessageServices {
	static Gson gson = new Gson();
	
	/**
	 * Get messages posted on a tree wall
	 * @param treeId Id of the tree
	 * @param listener Listener to notify when it is done
	 */
	public static void getTreeMessages(long treeId, TaskListener listener)
	{
		//build url
		String url = URL_BASE + URL_MESSAGE + SERVICE_GET;

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(PARAMETER_TREE_ID, String.valueOf(treeId));
		
		String jsonParams = gson.toJson(jsonObject);
		
		RequestExecutor requestExecutor = new RequestExecutor(jsonParams, url, RequestExecutor.RequestType.POST, listener);
		requestExecutor.execute();
	}
	
	/**
	 * Add a new message on a tree wall
	 * @param treeId Id of the tree
	 * @param msg Message to send
	 * @param listener Listener to notify when it is done
	 */
	public static void sendMessage(long treeId, Message msg, TaskListener listener)
	{
		//build url
		String url = URL_BASE + URL_MESSAGE + SERVICE_NEW;

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(PARAMETER_TREE_ID, String.valueOf(treeId));
		jsonObject.addProperty(JSON_SENDER, msg.getAuthor());
		jsonObject.addProperty(JSON_CONTENT, msg.getContent());
		
		String jsonParams = gson.toJson(jsonObject);
		
		RequestExecutor requestExecutor = new RequestExecutor(jsonParams, url, RequestExecutor.RequestType.POST, listener);
		requestExecutor.execute();
	}
	
	
	
}
