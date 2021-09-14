package ar.edu.ips.aus.android.test_activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TestActivity extends Activity {

	public static final String TEST_ACTIVITY = "TestActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TEST_ACTIVITY, "Llamada a OnCreate()");
		setContentView(R.layout.activity_test);

		Button btnImplicitCall = (Button) findViewById(R.id.button2);
		btnImplicitCall.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				String recipient = "pepe.sanchez@example.com";
				String textMessage = "Hola, hola ...";
				sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
				sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
				sendIntent.setType("text/plain");     // "text/plain" MIME

				//startActivity(Intent.createChooser(sendIntent, "Chooser Title"));
				startActivity(sendIntent);

//			if (sendIntent.resolveActivity(getPackageManager()) != null) {
//					startActivity(sendIntent);
//				}
			}

		});

		Button btnExplCall = (Button) findViewById(R.id.button1);
		btnExplCall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent explIntent = new Intent(TestActivity.this,
						ExtraActivity.class);
				startActivity(explIntent);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TEST_ACTIVITY, "Llamada a OnStart()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(TEST_ACTIVITY, "Llamada a OnRestart()");	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TEST_ACTIVITY, "Llamada a OnResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TEST_ACTIVITY, "Llamada a OnPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TEST_ACTIVITY, "Llamada a OnStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TEST_ACTIVITY, "Llamada a OnDestroy()");
	}
}
