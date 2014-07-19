package com.team68.chatbot68;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class MessageActivity extends ListActivity {
	

	ArrayList<Message> messages;
	ListApdapter adapter;
	EditText text;

	protected static final int RESULT_SPEECH = 1;
	static Random rand = new Random();
	static String sender;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		text = (EditText) this.findViewById(R.id.text);

		sender = "Team68";
		this.setTitle(sender);
		messages = new ArrayList<Message>();

		adapter = new ListApdapter(this, messages);
		setListAdapter(adapter);

	}

	public void sendMessage(View v) {
		/*
		 * Text Input
		 */
		InputMethodManager inputManager = 
		        (InputMethodManager) getApplicationContext().
		            getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(
		        this.getCurrentFocus().getWindowToken(),
		        InputMethodManager.HIDE_NOT_ALWAYS);
		
		String newMessage = text.getText().toString().trim();
		if (newMessage.length() > 0) {
			text.setText("");
			addNewMessage(new Message(newMessage, true));
			if (Network.hasConnection(getApplicationContext())){
				OnlineBot onlineBot = new OnlineBot(this);
				onlineBot.setRequest(newMessage);
				onlineBot.execute();
			} else {
				lostInternet();
			}
			
			
			

		}
	}
	
	public void lostInternet(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setTitle("Mất kết nối");
		builder1.setMessage("Vui lòng kết nối internet để tiếp tục trò chuyện");
		builder1.setCancelable(true);
		builder1.setNeutralButton(android.R.string.ok,
		        new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
		        dialog.cancel();
		    }
		});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}
	
	public void voiceText(View v) {
		/*
		 * Convert voice to text
		 */
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		try {
			startActivityForResult(intent, RESULT_SPEECH);

		} catch (ActivityNotFoundException a) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Oops! Your device doesn't support Speech to Text",
					Toast.LENGTH_SHORT);
			t.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				addNewMessage(new Message(text.get(0), true));
				OnlineBot onlineBot = new OnlineBot(this);
				onlineBot.setRequest(text.get(0));
				onlineBot.execute();
			}
			break;
		}
		}
	}

	void addNewMessage(Message m) {
		messages.add(m);
		adapter.notifyDataSetChanged();
		getListView().setSelection(messages.size() - 1);
	}

}