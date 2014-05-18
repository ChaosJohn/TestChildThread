package com.example.testchildthread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.testchildthread.ChildThread.WorkForMain;

public class MainActivity extends Activity {

	private ChildThread childThread = null;
	private Handler mainHandler = null, childHandler = null;
	private final int MSG_A = 0;
	private final int MSG_B = 1;
	private TextView textView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btnButton = (Button) findViewById(R.id.btn);
		textView = (TextView) findViewById(R.id.textView1);
		mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String string = (String) msg.obj;
				textView.setText(string);
			}
		};

		childThread = new ChildThread(new WorkForMain() {

			@Override
			public void doJob(Message msg) {
				// TODO Auto-generated method stub
				String str = (String) msg.obj;
				str = str + "...send to mainThread";
				Message toMainMessage = mainHandler.obtainMessage();
				toMainMessage.obj = str;
				mainHandler.sendMessage(toMainMessage);
			}
		});

		childThread.start();
		childHandler = childThread.getHandler();

		btnButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message toChildMessage = childHandler.obtainMessage();
				toChildMessage.obj = "This is sent to childThread";
				childHandler.sendMessage(toChildMessage);
			}
		});
	}
}
