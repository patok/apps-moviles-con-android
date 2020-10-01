package ar.edu.ips.aus.android.test_activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		Button btnExplicitCall = (Button) findViewById(R.id.button2);
		btnExplicitCall.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.setData(Uri.parse("mailto:pepe.sanchez@example.com"));
				String textMessage = "Hola, hola ...";
				sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
				sendIntent.setType("text/plain");     // "text/plain" MIME

				startActivity(Intent.createChooser(sendIntent, "Chooser Title"));

//			if (sendIntent.resolveActivity(getPackageManager()) != null) {
//					startActivity(sendIntent);
//				}
			}

		});

		Button btnImplicitCall = (Button) findViewById(R.id.button1);
		btnImplicitCall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent explIntent = new Intent(TestActivity.this,
						ExtraActivity.class);
				startActivity(explIntent);
			}
		});
	}
}
