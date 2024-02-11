package com.romanbutov.launcher;

import android.app.ListActivity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import static android.util.Log.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent launcherIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolves = getPackageManager().queryIntentActivities(launcherIntent, 0);

        // prepare usages sorted by the last time
        UsageStatsManager usm = (UsageStatsManager) getSystemService("usagestats");
        List<UsageStats> usages = Collections.emptyList();
        if (usm != null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            usages = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, cal.getTimeInMillis(), System.currentTimeMillis());
            usages.sort(Comparator.comparingLong(UsageStats::getLastTimeUsed).reversed());
        }

        // prepare LRU sorted component list
        List<ResolveInfo> listItems = new ArrayList<ResolveInfo>(resolves.size());
        for (UsageStats usage : usages) {
            Iterator<ResolveInfo> it = resolves.iterator();
            while (it.hasNext()) {
                ResolveInfo ri = it.next();
                if (usage.getPackageName().equals(ri.activityInfo.applicationInfo.packageName)) {
                    listItems.add(ri);
                    it.remove();
                }
            }
        }
        listItems.addAll(resolves);
        setListAdapter(new ListAdapter(this, listItems));

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                ResolveInfo ri = (ResolveInfo) parent.getItemAtPosition(position);
                launcherIntent
                    .setClassName(ri.activityInfo.applicationInfo.packageName, ri.activityInfo.name)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launcherIntent);
            }
        });
    }

    private static final String TAG = "launcher";
}
