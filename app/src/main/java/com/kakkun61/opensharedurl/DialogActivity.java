package com.kakkun61.opensharedurl;

import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DialogActivity extends ListActivity {
    private AppAdapter adapter;
    private Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        Bundle extras = getIntent().getExtras();
        if (extras == null) return;

        uri = Uri.parse(extras.getString(Intent.EXTRA_TEXT));

        PackageManager pm = getPackageManager();
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, uri);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(urlIntent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

        adapter = new AppAdapter(pm, resolveInfos);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ResolveInfo resolveInfo = adapter.getItem(position);
        if (resolveInfo == null) return;

        ActivityInfo activityInfo = resolveInfo.activityInfo;
        ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setComponent(name);

        startActivity(intent);

        finish();
    }

    private class AppAdapter extends ArrayAdapter<ResolveInfo> {
        private PackageManager pm;

        public AppAdapter(PackageManager pm, List<ResolveInfo> infos) {
            super(DialogActivity.this, R.layout.list_row, infos);
            this.pm = pm;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_row, parent, false);
                convertView.setTag(convertView.findViewById(R.id.item));
            }

            TextView text = (TextView) convertView.getTag();
            ResolveInfo resolveInfo = getItem(position);
            if (resolveInfo == null) return convertView;
            text.setText(resolveInfo.loadLabel(pm));
            Drawable icon = resolveInfo.loadIcon(pm);
            icon.setBounds(0, 0, 116, 116);
            text.setCompoundDrawablesRelative(icon, null, null, null);

            return convertView;
        }
    }
}
