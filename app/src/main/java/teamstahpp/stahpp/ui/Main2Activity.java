package teamstahpp.stahpp.ui;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import teamstahpp.stahpp.Adapter.AppAdapter;
import teamstahpp.stahpp.R;
import teamstahpp.stahpp.model.AppUsageStats;

import static android.app.usage.UsageStatsManager.INTERVAL_BEST;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = Main2Activity.class.getSimpleName();
    RecyclerView mRecyclerView;
    AppAdapter mAppAdapter;
    UsageStatsManager mUsageStatsManager;
    Button mOpenSettingsBtn;
    GraphView graph;
    ArrayList<String> selectedPackages;
    private PackageManager packageManager;
    Button b1;
    static long counter = 0;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        packageManager = getPackageManager();


        graph = (GraphView) findViewById(R.id.graph);
        /*BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        Paint paint = new Paint();
        paint.setStrokeWidth(50);
        graph.addSeries(series);

        graph.getViewport().setXAxisBoundsManual(true);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Facebook", "Instagram", "Google", "Twittwe", "Plurk"});
//staticLabelsFormatter.setVerticalLabels(new String[] {"low", "middle", "high"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);*/


        Toolbar myToolbar = (Toolbar) findViewById(R.id.complete_toolbar);
        setSupportActionBar(myToolbar);
        selectedPackages = new ArrayList<>();
        /*b1 = (Button)findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
            }
        });
*/      final String PREFS_NAME = "settings";
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        System.out.println(settings.getAll().toString());
        Set<String> keys = settings.getAll().keySet();
        for (String key : keys) {
            System.out.println(settings.getBoolean(key, true));
            boolean t = settings.getBoolean(key, true);
            if (t == true) {
                selectedPackages.add(key);
            }
        }
        //selectedPackages = new ArrayList<>(keys);
        //System.out.println(selectedPackages.toString());
        mUsageStatsManager = (UsageStatsManager) getSystemService("usagestats");
        mAppAdapter = new AppAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.rec_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(mAppAdapter);
        mOpenSettingsBtn = (Button)findViewById(R.id.button_open_usage_setting);
        List<UsageStats> usageStatsList =
                getUsageStatistics(INTERVAL_BEST);
        updateAppsList(usageStatsList);

        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, new Intent(this, MyTestService.class), PendingIntent.FLAG_CANCEL_CURRENT );

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);



        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10 * 1000, servicePendingIntent);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<UsageStats> getUsageStatistics(int intervalType) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        if (queryUsageStats.size() == 0) {
            Log.i(TAG, "The user may not allow the access to apps usage. ");
            Toast.makeText(getApplicationContext(),
                    "App Usage not enabled",
                    Toast.LENGTH_LONG).show();
            mOpenSettingsBtn.setVisibility(View.VISIBLE);
            mOpenSettingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            });
        }
        return queryUsageStats;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void updateAppsList(List<UsageStats> usageStatsList) {

        List<AppUsageStats> customUsageStatsList = new ArrayList<>();
        for (int i = 0; i < usageStatsList.size(); i++) {
            AppUsageStats customUsageStats = new AppUsageStats();
            customUsageStats.usageStats = usageStatsList.get(i);
            if (!selectedPackages.contains(customUsageStats.usageStats.getPackageName().toString())) {
                continue;
            }
            try {
                Drawable appIcon = getPackageManager()
                        .getApplicationIcon(customUsageStats.usageStats.getPackageName());
                customUsageStats.appIcon = appIcon;
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(TAG, String.format("App Icon is not found for %s",
                        customUsageStats.usageStats.getPackageName()));
                customUsageStats.appIcon = getDrawable(R.drawable.ic_android_black_24dp);
            }
            customUsageStatsList.add(customUsageStats);
        }
        if (customUsageStatsList.size() > 1) {
            Map<String, Long> timeGraph = new HashMap<>();
            for (AppUsageStats u: customUsageStatsList) {
                ApplicationInfo applicationInfo = null;
                String packageName = u.usageStats.getPackageName();
                try {
                    applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                } catch (final PackageManager.NameNotFoundException e) {}
                final String title = (String)((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : packageName);
                long timeUsed = TimeUnit.MILLISECONDS.toMinutes(u.usageStats.getTotalTimeInForeground());
                timeGraph.put(title,timeUsed);
            }
            System.out.println(timeGraph.toString());
            Set<String> t = timeGraph.keySet();
            String[] titles = t.toArray(new String[t.size()]);
            DataPoint[] times = new DataPoint[titles.length];

            for (int i = 0; i < times.length; i++) {

                times[i] = new DataPoint(i,timeGraph.get(titles[i]));
            }
            System.out.println(Arrays.toString(times));
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(times);
            Paint paint = new Paint();
            paint.setStrokeWidth(50);
            graph.addSeries(series);

            graph.getViewport().setXAxisBoundsManual(true);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(titles);
            //staticLabelsFormatter.setVerticalLabels(new String[] {"low", "middle", "high"});
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        }

        mAppAdapter.setUsageStatsList(customUsageStatsList);
        mAppAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    /*private void addNotification() {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_android_black_24dp)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, MainActivity_working.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.complete_toolbar_menu, menu);
        return true;
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
