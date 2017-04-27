package teamstahpp.stahpp.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
    ArrayList<String> selectedPackages;
    Button b1;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        b1 = (Button)findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
            }
        });
        selectedPackages = getIntent().getStringArrayListExtra("packagelist");
        System.out.println(selectedPackages.toString());
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
        mAppAdapter.setUsageStatsList(customUsageStatsList);
        mAppAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_android_black_24dp)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
