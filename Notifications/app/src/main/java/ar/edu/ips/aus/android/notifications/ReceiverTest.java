package ar.edu.ips.aus.android.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ReceiverTest extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("RECEIVER_TEST", "SMS received");
		Toast.makeText(context, "Hemos interceptado un SMS entrante!!", Toast.LENGTH_LONG).show();
	}

}
