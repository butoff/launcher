package com.romanbutov.launcher;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;;
import android.widget.TextView;

import java.util.List;

class ListAdapter extends ArrayAdapter<ResolveInfo> {

    ListAdapter(Context context, List<ResolveInfo> resolveInfos) {
        super(context, android.R.layout.activity_list_item, android.R.id.text1, resolveInfos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewGroup v = (ViewGroup) super.getView(position, convertView, parent);
        TextView tv = v.findViewById(android.R.id.text1);
        ImageView iv = v.findViewById(android.R.id.icon);

        ResolveInfo item = getItem(position);
        PackageManager pm = getContext().getPackageManager();
        tv.setText(item.loadLabel(pm));
        iv.setImageDrawable(item.loadIcon(pm));
        return v;
    }
}
