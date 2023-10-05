package ar.edu.ips.aus.android.notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationActivity extends Activity {


	protected static final int NOTIFICATION_ID = 0;
	public static final String CHANNEL_ID = "GuadalCanal";

	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "MiCanal";
			String description = "Un canal de prueba";
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

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

		createNotificationChannel();
		Button button3 = (Button) findViewById(R.id.notification_button1);
		button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// create a pending intent
				Intent notificationIntent = new Intent(getApplicationContext(),
						NotificationActivity.class);
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
						notificationIntent, 0);

				// user builder to prepare notification
				NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
						getApplicationContext(), CHANNEL_ID)
						//.setTicker(getResources().getString(R.string.ticker_text))
						.setSmallIcon(android.R.drawable.stat_sys_warning)
						.setAutoCancel(true)
						.setContentTitle(getResources().getString(R.string.notification_title))
						.setContentText(getResources().getString(R.string.notification_text))
						.setContentIntent(pendingIntent);

				// pass the Notification to the NotificationManager
				NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);
				notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
			}
		});
		
		Button button4 = (Button) findViewById(R.id.notification_button2);
		
		button4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NotificationManagerCompat notificationManager =  NotificationManagerCompat.from(NotificationActivity.this);
				notificationManager.cancelAll();
			}
		});

	}
}
