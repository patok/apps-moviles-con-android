package ar.edu.ips.aus.seminario2.preferences_sample.storagesharedpreferences;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class StorageSharedPreferences extends Activity {

	TextView campo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storage_shared_preferences);
		
        campo = (TextView) findViewById(R.id.editText1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.storage_shared_preferences, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				startPreferencesActivity();
				return true;
			case R.id.exit:
				this.finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void startPreferencesActivity() {
		Intent intent = new Intent(this.getApplicationContext(), MyPrefsActivity.class);
		startActivity(intent);
	}

	@Override
    protected void onResume() {
    	super.onResume();
    	
    	// Cada vez que se resume (muestra) la Activity, vamos a incrementar un contador
    	
    	// Pido al sistema un SharedPreferences específica para esta Activity
    	SharedPreferences pref = getPreferences(MODE_PRIVATE);
    	
    	// Pido al sistema un SharedPreferences de nombre "miConfig", global a mi Aplicación
    	//SharedPreferences pref2 = getSharedPreferences("miConfig", MODE_PRIVATE);
    	
    	int cant = pref.getInt("cant_vistas", 0);
    	cant = cant + 1;
    	
    	// Para modificar un valor de un SharedPreferences, primero obtengo un Editor
    	SharedPreferences.Editor editor = pref.edit();
    	// luego hago las modificaciones que quiero con "putBoolean", "putInt", "putLong", "putFloat" o "putString"
    	editor.putInt("cant_vistas", cant);
    	// por último llamo al método "commit" para hacer persistente los cambios
    	editor.commit();
    	
    	// muestro el contador en pantalla
        campo.setText(Integer.toString(cant));
    }
}
