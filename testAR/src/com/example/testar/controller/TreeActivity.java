package com.example.testar.controller;

import static com.example.testar.services.Constants.JSON_CONTENT;
import static com.example.testar.services.Constants.JSON_SENDER;
import static com.example.testar.services.Constants.PARAMETER_TREE_ID;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testar.R;
import com.example.testar.model.Message;
import com.example.testar.services.MessageServices;
import com.example.testar.utils.ErrorCode;
import com.example.testar.utils.SpinnerDialog;
import com.example.testar.utils.TaskListener;

public class TreeActivity extends Activity {
	private static final long DEFAULT_ID = -1;

	private ListView listview;
	private TextView noTree;
	private Button publishButton;
	private EditText editSender, editContent;
	private Context context;
	private SpinnerDialog waitingSpinnerDialog;
	private long treeId;
	
	private TaskListener getMessages = new TaskListener() {

		@Override
		public void onSuccess(String content) {
			waitingSpinnerDialog.dismiss();
			
			JSONObject response;
			try {
				response = new JSONObject(content);
				if (response.has(JSON_CONTENT)){
					noTree.setVisibility(View.GONE);
					JSONArray messages = response.getJSONArray(JSON_CONTENT);
					List<Message> messagesList = new ArrayList<Message>();
					for(int i=0; i<messages.length(); i++){
						JSONObject msg = messages.getJSONObject(i);
						String sender = msg.getString(JSON_SENDER);
						String contentMsg = msg.getString(JSON_CONTENT);
						messagesList.add(new Message(contentMsg, sender));
					}
					MsgListAdapter customAdapter = new MsgListAdapter(context, R.layout.message_item, messagesList);
					listview.setAdapter(customAdapter);
					listview.setVisibility(View.VISIBLE);
					
				} else {
					noTree.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
				}
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
			alertDialogBuilder.setNeutralButton(R.string.neutral_button, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();

				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tree);

		FragmentManager fm = getFragmentManager();
		waitingSpinnerDialog = new SpinnerDialog(getString(R.string.loading));
		waitingSpinnerDialog.show(fm, "");

		context = this;

		Intent intent = getIntent();
		treeId = intent.getLongExtra(PARAMETER_TREE_ID, DEFAULT_ID);

		listview = (ListView) findViewById(R.id.msg_listview);
		noTree = (TextView) findViewById(R.id.tv_notree);
		publishButton = (Button) findViewById(R.id.publish_msg);
		editContent = (EditText) findViewById(R.id.edit_msg);
		editSender = (EditText) findViewById(R.id.edit_author);
		
		publishButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String contentMsg = editContent.getText().toString();
				String authorMsg = editSender.getText().toString();
				
				if (contentMsg.equals("") || authorMsg.equals("")){
					Toast.makeText(context, R.string.empty_field, Toast.LENGTH_LONG).show();
				} else {
					FragmentManager fm = getFragmentManager();
					waitingSpinnerDialog = new SpinnerDialog(getString(R.string.sending));
					waitingSpinnerDialog.show(fm, "");
					Message newMessage = new Message(contentMsg, authorMsg);
					
					TaskListener postMessage = new TaskListener() {
						
						@Override
						public void onSuccess(String content) {
							Toast.makeText(context, R.string.post_success, Toast.LENGTH_SHORT).show();
							MessageServices.getTreeMessages(treeId, getMessages);
						}
						
						@Override
						public void onFailure(ErrorCode errCode) {
							waitingSpinnerDialog.dismiss();
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
							alertDialogBuilder.setMessage(R.string.post_failed);
							alertDialogBuilder.setNeutralButton(R.string.neutral_button, null);
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
							
						}
					};
					MessageServices.sendMessage(treeId, newMessage, postMessage);
					
				}
			}
		});

		MessageServices.getTreeMessages(treeId, getMessages);
	}


	public class MsgListAdapter extends ArrayAdapter<Message>{
		
		public MsgListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}
		
		public MsgListAdapter(Context context, int resource, List<Message> items) {
			super(context, resource, items);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater vi;
				vi = LayoutInflater.from(getContext());
				view = vi.inflate(R.layout.message_item, null);
			}
			Message msg = (Message) getItem(position);
			if (msg != null) {
				TextView contentTextView = (TextView) view.findViewById(R.id.msg_content);
				TextView authorTextView = (TextView) view.findViewById(R.id.msg_author);

				if (contentTextView != null) {
					contentTextView.setText(msg.getContent());
				}
				if (authorTextView != null) {
					authorTextView.setText(msg.getAuthor());
				}

			}
			return view;
		} 
	}
}
