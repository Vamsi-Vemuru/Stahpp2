package teamstahpp.stahpp.ui;

/**
 * Created by ISR on 02-May-17.
 */

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import java.util.ArrayList;
import java.util.List;

import teamstahpp.stahpp.R;

public class SettingsFragment extends PreferenceFragment {
    static ArrayList<ApplicationInfo> listOfApps;
    public List<PackageInfo> packInfoList;
    List<PackageInfo> allPacks = new ArrayList<>();
    ArrayList<String> listViewItems = new ArrayList<String>();
    CheckBoxPreference[] listOfCheckBoxes;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        int i = 0;

        listOfApps = new ArrayList<>();
        PackageManager pm = getActivity().getPackageManager();
        packInfoList = pm.getInstalledPackages(0);
        for (PackageInfo packInf : packInfoList) {
            //   listOfApps.add(pm.getApplicationLabel(packInf.applicationInfo).toString());


            ApplicationInfo app = packInf.applicationInfo;
            //checks for flags; if flagged, check if updated system app
                       System.out.println((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)+"||" + (app.flags & ApplicationInfo.FLAG_INSTALLED));
            if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (app.flags & ApplicationInfo.FLAG_INSTALLED) != 0) {
                listOfApps.add(app);
                //it's a system app, not interested
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //Discard this one
                //in this case, it should be a user-installed app
                String label = (String)pm.getApplicationLabel(app);
                //listOfApps.add(app);
                listViewItems.add(label);
                allPacks.add(packInf);

            } else {
                //              listOfApps.add(app);
            }

        }
        listOfCheckBoxes = new CheckBoxPreference[listOfApps.size()];
        for (ApplicationInfo s : listOfApps) {
            CheckBoxPreference cb = new CheckBoxPreference(getActivity());
            cb.setTitle(pm.getApplicationLabel(s).toString());
            cb.setKey(s.packageName);
            System.out.println();
            listOfCheckBoxes[i++] = cb;
            ((PreferenceScreen) getPreferenceManager().findPreference("settings")).addPreference(cb);
        }
    }
}