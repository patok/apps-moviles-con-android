package ar.edu.ips.aus.android.notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationActivity extends Activity {

	
	protected static final int NOTIFICATION_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		
		Button button = (Button) findViewById(R.id.toast_button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Esto es un toast!", Toast.LENGTH_LONG).show();				
			}
		});
		
		Button button2 = (Button) findViewById(R.id.toast_button2);
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.toast_layout,
				                               (ViewGroup) findViewById(R.id.toast_layout_root));

				TextView text = (TextView) layout.findViewById(R.id.text);
				text.setText("Un toast customizado");

				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
			}
		});
		
		Button button3 = (Button) findViewById(R.id.notification_button1);
		button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// create a pending intent
				Intent notificationIntent = new Intent(getApplicationContext(),
						NotificationActivity.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
						notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

				// user builder to prepare notification
				Notification.Builder notificationBuilder = new Notification.Builder(
						getApplicationContext())
						.setTicker(getResources().getString(R.string.ticker_text))
						.setSmallIcon(android.R.drawable.stat_sys_warning)
						.setAutoCancel(true)
						.setContentTitle(getResources().getString(R.string.notification_title))
						.setContentText(getResources().getString(R.string.notification_text))
						.setContentIntent(pendingIntent);

				// pass the Notification to the NotificationManager
				NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
			}
		});
		
		Button button4 = (Button) findViewById(R.id.notification_button2);
		
		button4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.cancelAll();
			}
		});

	}
}
