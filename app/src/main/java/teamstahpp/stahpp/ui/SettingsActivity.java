package teamstahpp.stahpp.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import teamstahpp.stahpp.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        setContentView(R.layout.settings_container);
        getFragmentManager().beginTransaction().add(R.id.settings_layout, new SettingsFragment()).commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.complete_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

    }
}