package teamstahpp.stahpp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import teamstahpp.stahpp.R;

/**
 * Created by ISR on 02-May-17.
 */

public class MainActivity extends AppCompatActivity {

    Boolean firstTime=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            firstTime=true;
            //the app is being launched for first time:
            // Display the fragment as the main content.
            setContentView(R.layout.settings_container);
            getFragmentManager().beginTransaction().add(R.id.settings_layout, new SettingsFragment()).commit();
            //  getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
            Toolbar myToolbar = (Toolbar) findViewById(R.id.complete_toolbar);
            setSupportActionBar(myToolbar);

            Log.d("Comments", "First time");
            // first time task
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }else{
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
            //setContentView(R.layout.activity_main);
           /* Toolbar myToolbar = (Toolbar) findViewById(R.id.complete_toolbar);
            setSupportActionBar(myToolbar);*/
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.complete_toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (firstTime) {
            MenuItem itemDone = menu.findItem(R.id.action_done);
            itemDone.setVisible(true);
            MenuItem itemSettings = menu.findItem(R.id.action_settings);
            itemSettings.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_done:
                Intent intentMain = new Intent(this, MainActivity.class);
                startActivity(intentMain);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}

