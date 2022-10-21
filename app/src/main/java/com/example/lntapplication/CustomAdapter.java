package com.example.lntapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<BleDeviceDetails> {
    private ArrayList<BleDeviceDetails> dataSet;
    Context mContext;

    public CustomAdapter(ArrayList<BleDeviceDetails> data, Context context) {
        super(context, R.layout.listview, data);
        this.dataSet = data;
        this.mContext=context;

    }
    private static class ViewHolder {
        TextView txtName;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BleDeviceDetails dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.textView13);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }



        viewHolder.txtName.setText(dataModel.getAddress());

        // Return the completed view to render on screen
        return convertView;
    }
}
