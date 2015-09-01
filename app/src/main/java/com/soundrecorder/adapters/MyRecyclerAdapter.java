package com.soundrecorder.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soundrecorder.R;
import com.soundrecorder.beans.Items;

import java.util.List;

/**
 * Created by BLUEHORSE 123 on 8/28/2015.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private List<Items> items;
    private int itemLayout;

    public MyRecyclerAdapter(List<Items> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyRecyclerAdapter.ViewHolder holder, int position) {
        Items item = items.get(position);
        holder.txt1.setText(item.getRecord_name());
        holder.txt2.setText(item.getDuration());
        holder.txt3.setText(String.valueOf(item.getSize()));
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt1,txt2,txt3;
        public ViewHolder(View itemView) {
            super(itemView);
            txt1 = (TextView) itemView.findViewById(R.id.name_song);
            txt2 = (TextView) itemView.findViewById(R.id.txt_duration);
            txt3 = (TextView) itemView.findViewById(R.id.txt_size);
        }
    }
}
