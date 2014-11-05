package com.kakkun61.opensharedurl;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri = Uri.parse(getIntent().getExtras().getString(Intent.EXTRA_TEXT));
        Log.d("Open Shared URL", "URI: " + uri);

        Intent urlIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(Intent.createChooser(urlIntent, getResources().getString(R.string.open_url_dialog_title)));

        finish();
    }
}
