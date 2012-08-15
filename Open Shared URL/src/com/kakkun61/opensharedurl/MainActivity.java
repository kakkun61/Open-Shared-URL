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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
    private AppAdapter adapter;
    private Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uri = Uri.parse(getIntent().getExtras().getString(Intent.EXTRA_TEXT));

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
      ActivityInfo activityInfo = resolveInfo.activityInfo;
      ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);

      Intent intent = new Intent(Intent.ACTION_MAIN);
      intent.addCategory(Intent.CATEGORY_LAUNCHER);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
      intent.setData(uri);
      intent.setComponent(name);

      startActivity(intent);

      finish();
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
                convertView.setTag(convertView.findViewById(R.id.item));
            }

            TextView text = (TextView) convertView.getTag();
            text.setText(getItem(position).loadLabel(pm));
            text.setCompoundDrawablesWithIntrinsicBounds(getItem(position).loadIcon(pm), null, null, null);

            return convertView;
        }
    }
}
