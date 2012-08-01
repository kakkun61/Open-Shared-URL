package com.kakkun61.opensharedurl;

import java.util.Collections;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = (TextView) findViewById(R.id.text);
        PackageManager pm = getPackageManager();
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(urlIntent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        StringBuilder sb = new StringBuilder();
        for (ResolveInfo resolveInfo : resolveInfos) {
            sb.append(resolveInfo.loadLabel(pm) + "\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        text.setText(sb.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
