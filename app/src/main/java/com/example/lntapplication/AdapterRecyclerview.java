package com.example.lntapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRecyclerview extends RecyclerView.Adapter<AdapterRecyclerview.Myviewholder> {
    Context c;
    List<BleDeviceDetails> list;
    private bleListener reportListener;

    public AdapterRecyclerview(Context c, List<BleDeviceDetails> list, bleListener reportListener) {
        this.c = c;
        this.list = list;
        this.reportListener = reportListener;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.listview, parent, false);
        return new Myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        BleDeviceDetails details = list.get(position);
        holder.textView.setText(details.getName());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        TextView textView;
        ConstraintLayout layout;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layoutlist);
            textView = itemView.findViewById(R.id.textView13);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reportListener.onItemClick(getAdapterPosition());
                }
            });
        }

    }
}
