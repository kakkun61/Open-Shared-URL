package com.kakkun61.opensharedurl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            Preference showLauncherPreference = findPreference("show_launcher");

            showLauncherPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int newState;
                    if ((Boolean) newValue) {
                        newState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
                    } else {
                        newState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                    }
                    String packageName = getActivity().getPackageName();
                    ComponentName componentName = new ComponentName(packageName, packageName + ".MainActivity");
                    getActivity().getPackageManager().setComponentEnabledSetting(componentName, newState, PackageManager.DONT_KILL_APP);
                    return true;
                }
            });

            Preference shareSamplePreference = findPreference("share_sample");

            shareSamplePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "http://kakkun61.com/");
                    intent.setType("text/plain");
                    startActivity(intent);
                    return true;
                }
            });
        }
    }
}