package teamstahpp.stahpp.ui;

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
    static ArrayList<String> listOfApps;
    public List<PackageInfo> packInfoList;
    CheckBoxPreference[] listOfCheckBoxes;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        int i = 0;

        listOfApps = new ArrayList<>();
        PackageManager pm = getActivity().getPackageManager();
        packInfoList = pm.getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo packInf : packInfoList) {
            listOfApps.add(pm.getApplicationLabel(packInf.applicationInfo).toString());
        }
        listOfCheckBoxes = new CheckBoxPreference[listOfApps.size()];
        for (String s : listOfApps) {
            CheckBoxPreference cb = new CheckBoxPreference(getActivity());
            cb.setTitle(s);
            cb.setKey(String.valueOf(i + 1));
            System.out.println();
            listOfCheckBoxes[i++] = cb;
            ((PreferenceScreen) getPreferenceManager().findPreference("settings")).addPreference(cb);
        }
    }
}

