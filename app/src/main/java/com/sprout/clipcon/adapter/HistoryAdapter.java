package com.sprout.clipcon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sprout.clipcon.R;
import com.sprout.clipcon.model.History;
import com.sprout.clipcon.model.Member;

import java.util.ArrayList;

/**
 * Created by Yongwon on 2017. 4. 30..
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private Context context;
    private ArrayList<History> historyList;

    public HistoryAdapter(Context context, ArrayList<History> historyArrayList) {
        this.context = context;
        this.historyList = historyArrayList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history, null);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);

        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        History history = historyList.get(position);
        holder.sender.setText(history.getSender());
        holder.description.setText(history.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("choi", position+"번째 클릭");
                Toast.makeText(context, position+"번째가 클릭됐음", Toast.LENGTH_SHORT).show();
            }
        });

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
