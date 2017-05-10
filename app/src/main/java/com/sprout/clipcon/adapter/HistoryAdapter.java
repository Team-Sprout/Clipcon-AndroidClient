package com.sprout.clipcon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sprout.clipcon.R;
import com.sprout.clipcon.model.History;
import com.sprout.clipcon.model.Member;

import java.util.ArrayList;

/**
 * Created by Yongwon on 2017. 4. 30..
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private ArrayList<History> historyList;

    public HistoryAdapter(ArrayList<History> historyArrayList) {
        historyList = historyArrayList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history, null);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);

        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.sender.setText(history.getSender());
        holder.description.setText(history.getDescription());

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView sender;
        TextView description;

        public HistoryViewHolder(final View historyView) {
            super(historyView);
            sender = (TextView)historyView.findViewById(R.id.history_sender);
            description = (TextView)historyView.findViewById(R.id.history_description);
        }
    }
}
