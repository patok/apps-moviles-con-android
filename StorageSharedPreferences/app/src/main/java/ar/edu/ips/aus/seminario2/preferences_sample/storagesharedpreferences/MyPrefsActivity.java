package ar.edu.ips.aus.seminario2.preferences_sample.storagesharedpreferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class MyPrefsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
