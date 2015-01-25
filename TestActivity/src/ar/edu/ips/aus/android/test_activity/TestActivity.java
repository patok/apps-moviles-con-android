package ar.edu.ips.aus.android.test_activity;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
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
				String textMessage = "Hola, hola ...";
				sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
				sendIntent.setType(HTTP.PLAIN_TEXT_TYPE); // "text/plain" MIME
															// type

				startActivity(sendIntent);

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
