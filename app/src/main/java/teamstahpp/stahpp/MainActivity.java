package teamstahpp.stahpp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listview ;
    ArrayList<String> listViewItems = new ArrayList<String>();
    List<PackageInfo> allPacks = new ArrayList<PackageInfo>();
    SparseBooleanArray sparseBooleanArray ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageManager pm = getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        //List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        List<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();
        //List<String> installedAppsStr = new ArrayList<String>();
        for(PackageInfo pack : packs) {
            ApplicationInfo app = pack.applicationInfo;
            //checks for flags; if flagged, check if updated system app
            if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                installedApps.add(app);
                //it's a system app, not interested
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //Discard this one
                //in this case, it should be a user-installed app
                String label = (String)pm.getApplicationLabel(app);
                listViewItems.add(label);
                allPacks.add(pack);

            } else {
                installedApps.add(app);
            }
        }
        listview = (ListView)findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (MainActivity.this,
                        android.R.layout.simple_list_item_multiple_choice,
                        android.R.id.text1, listViewItems );

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                sparseBooleanArray = listview.getCheckedItemPositions();

                String ValueHolder = "" ;

                int i = 0 ;

                while (i < sparseBooleanArray.size()) {

                    if (sparseBooleanArray.valueAt(i)) {

                        ValueHolder += listViewItems.get(sparseBooleanArray.keyAt(i)) + ",";
                    }

                    i++ ;
                }

                ValueHolder = ValueHolder.replaceAll("(,)*$", "");

                Toast.makeText(MainActivity.this, "ListView Selected Values = " + ValueHolder, Toast.LENGTH_LONG).show();

            }
        });

        Button submit = (Button)findViewById(R.id.button3);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                submit();
            }
        });
    }
    public void submit() {
        sparseBooleanArray = listview.getCheckedItemPositions();

        ArrayList<String> selectedPacks = new ArrayList<String>();
        int i = 0;
        while(i < sparseBooleanArray.size())
        {
            if(sparseBooleanArray.valueAt(i)) {
                selectedPacks.add(allPacks.get(sparseBooleanArray.keyAt(i)).packageName);
            }
            i++;
        }
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putStringArrayListExtra("packagelist", selectedPacks);
        startActivity(intent);
    }
}
