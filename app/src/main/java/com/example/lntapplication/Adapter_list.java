package com.example.lntapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_list extends RecyclerView.Adapter<Adapter_list.myviewholder> {
    List<Data_Model_Search> list;
    Context context;
//    List<String> tempList;

    public Adapter_list(List<Data_Model_Search> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list, parent, false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
//Initial Data model
        Data_Model_Search model = list.get(position);
        holder.language.setText(model.getSerialNo());
        holder.publisher.setText(model.getSapNo());
        holder.head_title.setText(model.getDrawingNo());
        holder.head_subject.setText(model.getContractor());
        holder.h1.setText("Contractor Name :");
        holder.h2.setText("Drawing ID :");
        holder.h3.setText("Sap NO :");
        holder.h4.setText("Serial Number :");

//Change color if data found
        String Colorname =model.getColor();
        switch (Colorname)
        {
            case "Green":

                holder.ListLayout.setBackgroundColor(Color.rgb(46, 139, 87));
                holder.head_subject.setTextColor(Color.parseColor("#FFFFFF"));
                holder.head_title.setTextColor(Color.parseColor("#FFFFFF"));
                holder.publisher.setTextColor(Color.parseColor("#FFFFFF"));
                holder.language.setTextColor(Color.parseColor("#FFFFFF"));
                holder.h4.setTextColor(Color.parseColor("#FFFFFF"));
                holder.h3.setTextColor(Color.parseColor("#FFFFFF"));
                holder.h2.setTextColor(Color.parseColor("#FFFFFF"));
                holder.h1.setTextColor(Color.parseColor("#FFFFFF"));
            break;
//
//            case "Red":
//                holder.ListLayout.setBackgroundColor(Color.RED);
//                holder.head_subject.setTextColor(Color.parseColor("#FFFFFF"));
//                holder.head_title.setTextColor(Color.parseColor("#FFFFFF"));
//                holder.publisher.setTextColor(Color.parseColor("#FFFFFF"));
//                holder.language.setTextColor(Color.parseColor("#FFFFFF"));
//                holder.h1.setTextColor(Color.parseColor("#FFFFFF"));
//                holder.h2.setTextColor(Color.parseColor("#FFFFFF"));
//                holder.h3.setTextColor(Color.parseColor("#FFFFFF"));
//                holder.h4.setTextColor(Color.parseColor("#FFFFFF"));
//                break;
            case "Normal":
                holder.ListLayout.setBackgroundColor(Color.parseColor("#F0FFFF"));
                holder.head_subject.setTextColor(Color.BLACK);
                holder.head_title.setTextColor(Color.BLACK);
                holder.publisher.setTextColor(Color.BLACK);
                holder.language.setTextColor(Color.BLACK);
                holder.h1.setTextColor(Color.BLACK);
                holder.h2.setTextColor(Color.BLACK);
                holder.h3.setTextColor(Color.BLACK);
                holder.h4.setTextColor(Color.BLACK);
                break;
//            holder.ListLayout.setBackgroundColor(Color.RED);
//            holder.head_subject.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.head_title.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.publisher.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.language.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h1.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h2.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h3.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h4.setTextColor(Color.parseColor("#FFFFFF"));

        }
//
//        if (model.getColor() == "Green") {
////            holder.cardView.setCardBackgroundColor(Color.rgb(46, 139, 87));
//            holder.ListLayout.setBackgroundColor(Color.rgb(46, 139, 87));
//            holder.head_subject.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.head_title.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.publisher.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.language.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h4.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h3.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h2.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h1.setTextColor(Color.parseColor("#FFFFFF"));
//
//        }  if (model.getColor() == "Red"){
//            holder.ListLayout.setBackgroundColor(Color.RED);
//            holder.head_subject.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.head_title.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.publisher.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.language.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h1.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h2.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h3.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.h4.setTextColor(Color.parseColor("#FFFFFF"));
//        }else {
//            holder.ListLayout.setBackgroundColor(Color.parseColor("#F0FFFF"));
//            holder.head_subject.setTextColor(Color.BLACK);
//            holder.head_title.setTextColor(Color.BLACK);
//            holder.publisher.setTextColor(Color.BLACK);
//            holder.language.setTextColor(Color.BLACK);
//            holder.h1.setTextColor(Color.BLACK);
//            holder.h2.setTextColor(Color.BLACK);
//            holder.h3.setTextColor(Color.BLACK);
//            holder.h4.setTextColor(Color.BLACK);
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class myviewholder extends RecyclerView.ViewHolder {
        TextView Subject, Title, publisher, author, edition, language, access_No, head_subject, head_title, expand, minimize;
        LinearLayout list_layout;
        CardView cardView, card_details;
        TextView h1, h2, h3, h4;
        ConstraintLayout ListLayout;
        ImageView Locate;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            //Binding components
            ListLayout = itemView.findViewById(R.id.LayoutList);
            Subject = itemView.findViewById(R.id.Subject);
//            expand = itemView.findViewById(R.id.expand);
//            minimize = itemView.findViewById(R.id.minimize);
            Title = itemView.findViewById(R.id.Booktitle);
            list_layout = itemView.findViewById(R.id.list_layout);
            cardView = itemView.findViewById(R.id.cardView);
            card_details = itemView.findViewById(R.id.cardView_Details);
            publisher = itemView.findViewById(R.id.Publisher);
            author = itemView.findViewById(R.id.Authorname);
            edition = itemView.findViewById(R.id.Edition);
            language = itemView.findViewById(R.id.Language);
            access_No = itemView.findViewById(R.id.Access_No);
            head_subject = itemView.findViewById(R.id.Head_subject);
            head_title = itemView.findViewById(R.id.Head_Tilte);
            h1 = itemView.findViewById(R.id.textView6);
            h2 = itemView.findViewById(R.id.textView7);
            h3 = itemView.findViewById(R.id.textView8);
            h4 = itemView.findViewById(R.id.textView9);

//            Locate = itemView.findViewById(R.id.Locating);
        }
    }

//
//    public void removeItem(int position) {
//        list.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    public void restoreItem(SearchDataModel item, int position) {
//        list.add(position, item);/
//        notifyItemInserted(position);
//    }
//
//    public List<SearchDataModel> getData() {
//        return list;
//    }

    //Method for Search
    public void getFilter(@NonNull Object search_value) {
        for (Data_Model_Search row : list) {
            if (row.getRfidNo() != null) {
                if (row.getRfidNo().equals(search_value)) {
                    row.setColor("Green");
                    notifyDataSetChanged();
                    break;

                }
                else {
//                    if (row.getColor()=="Green")
//                    {
//                        notifyDataSetChanged();
//                        break;
//
//                    }else {
//                        row.setColor("Red");
//                        notifyDataSetChanged();
//                        break;
//                    }
                }
//                else {
//                    Toast.makeText(context.getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
//                    break;
//                }
            }
        }

    }

    public void RetrySearch() {
        for (Data_Model_Search row : list) {
            if (row.getColor().equals("Green"))
                row.setColor("Normal");
            notifyDataSetChanged();

        }
    }


}
