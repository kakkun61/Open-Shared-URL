package com.kakkun61.opensharedurl;

import java.util.Collections;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DialogActivity extends AppCompatActivity {
    private AppAdapter adapter;
    private Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        String uriString = extras.getString(Intent.EXTRA_TEXT);

        if (TextUtils.isEmpty(uriString)) {
            setContentView(R.layout.activity_dialog_empty);
            TextView message = findViewById(R.id.message);
            message.setText(R.string.not_url);
            return;
        }

        setTitle(uriString);

        uri = Uri.parse(uriString);

        PackageManager pm = getPackageManager();
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, uri);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(urlIntent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

        if (resolveInfos.isEmpty()) {
            setContentView(R.layout.activity_dialog_empty);
            TextView message = findViewById(R.id.message);
            message.setText(R.string.no_app);
            return;
        }

        setContentView(R.layout.activity_dialog);
        ListView listView = findViewById(R.id.list);
        adapter = new AppAdapter(pm, resolveInfos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView l, View v, int position, long id) {
                ResolveInfo resolveInfo = adapter.getItem(position);

                ActivityInfo activityInfo = resolveInfo.activityInfo;
                ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setComponent(name);

                startActivity(intent);

                finish();
            }
        });
    }

    private class AppAdapter extends ArrayAdapter<ResolveInfo> {
        private PackageManager pm;

        public AppAdapter(PackageManager pm, List<ResolveInfo> infos) {
            super(DialogActivity.this, R.layout.activity_dialog_list, infos);
            this.pm = pm;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.activity_dialog_list, parent, false);
                convertView.setTag(convertView.findViewById(R.id.item));
            }

            TextView text = (TextView) convertView.getTag();
            ResolveInfo resolveInfo = getItem(position);
            text.setText(resolveInfo.loadLabel(pm));
            Drawable icon = resolveInfo.loadIcon(pm);
            icon.setBounds(0, 0, 116, 116);
            text.setCompoundDrawablesRelative(icon, null, null, null);

            return convertView;
        }
    }
}
