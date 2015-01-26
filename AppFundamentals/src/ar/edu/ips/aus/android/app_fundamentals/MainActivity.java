package ar.edu.ips.aus.android.app_fundamentals;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	protected static final String TAG = "Main";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final EditText number = (EditText) findViewById(R.id.editText1);
		final Button button = (Button) findViewById(R.id.button1);

		// Link UI elements to actions in code		
		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String inputNumber = number.getText().toString();
		            Intent callIntent = new Intent(Intent.ACTION_CALL);          
		            callIntent.setData(Uri.parse("tel:"+ inputNumber));          
		            startActivity(callIntent);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
			}
		});

	}

}
