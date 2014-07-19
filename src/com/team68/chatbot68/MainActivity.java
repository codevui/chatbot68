package com.team68.chatbot68;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private ImageView statusImage;
	private TextView statusText;
	private Button btnStartChat;
	private Button btnQuit;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		statusImage = (ImageView) findViewById(R.id.imgStt);
		statusText = (TextView) findViewById(R.id.tvStt);
		btnStartChat = (Button) findViewById(R.id.btnStart);
		btnQuit = (Button) findViewById(R.id.btnQuit);
		
		if (Network.hasConnection(getApplicationContext())){
			statusImage.setImageResource(R.drawable.online_robot);
			statusText.setText("Robot đã online \n Nhấn bắt đầu để nói chuyện");
			btnStartChat.setVisibility(View.VISIBLE);
			btnQuit.setVisibility(View.GONE);
			
		} else {
			statusImage.setImageResource(R.drawable.offline_robot);
			statusText.setText("Bạn đã offline \n Kết nối internet rồi nhấn Thử lại");
			btnStartChat.setVisibility(View.GONE);
			btnQuit.setVisibility(View.VISIBLE);
		}
	}

	public void startChat(View v){
		Intent intent = new Intent(this, MessageActivity.class);
		startActivity(intent);
	}
	public void reloadActivity(View v){
		finish();
		startActivity(getIntent());
	}

}