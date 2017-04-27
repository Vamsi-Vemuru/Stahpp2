package teamstahpp.stahpp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import teamstahpp.stahpp.R;

/**
 * Created by jorso on 4/24/2017.
 */

public class Settings extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time:
            // Display the fragment as the main content.
            getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
            Log.d("Comments", "First time");
            // first time task
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }else{
            setContentView(R.layout.activity_main);
        }

    }
}
