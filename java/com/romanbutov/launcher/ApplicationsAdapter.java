package com.romanbutov.launcher;

import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class ApplicationsAdapter extends ArrayAdapter<ComponentName> {

    ApplicationsAdapter(Context context, List<ComponentName> applications) {
        super(context, android.R.layout.simple_list_item_1, applications);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = (TextView) super.getView(position, convertView, parent);
        tv.setText(getItem(position).flattenToShortString());
        return tv;
    }
}
