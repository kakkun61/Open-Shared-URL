package com.kakkun61.opensharedurl;

import java.util.Collections;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
    private AppAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager pm = getPackageManager();
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(urlIntent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

        adapter = new AppAdapter(pm, resolveInfos);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
      ResolveInfo resolveInfo = adapter.getItem(position);
      ActivityInfo activityInfo = resolveInfo.activityInfo;
      ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);

      Intent intent = new Intent(Intent.ACTION_MAIN);
      intent.addCategory(Intent.CATEGORY_LAUNCHER);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
      intent.setComponent(name);

      startActivity(intent);
    }

    private class AppAdapter extends ArrayAdapter<ResolveInfo> {
        private PackageManager pm;

        public AppAdapter(PackageManager pm, List<ResolveInfo> infos) {
            super(MainActivity.this, R.layout.list_row, infos);
            this.pm = pm;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_row, parent, false);
                convertView.setTag(convertView.findViewById(R.id.row));
            }

            TextView text = (TextView) convertView.getTag();
            text.setText(getItem(position).loadLabel(pm));
            Drawable icon = getItem(position).loadIcon(pm);
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            text.setCompoundDrawables(icon, null, null, null);

            return convertView;
        }
    }
}
