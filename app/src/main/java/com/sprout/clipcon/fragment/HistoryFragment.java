package com.sprout.clipcon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sprout.clipcon.R;
import com.sprout.clipcon.adapter.HistoryAdapter;
import com.sprout.clipcon.adapter.MemberAdapter;
import com.sprout.clipcon.model.History;
import com.sprout.clipcon.model.Member;
import com.sprout.clipcon.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Yongwon on 2017. 2. 8..
 */

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;

    private ArrayList<History> historyArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        System.out.println("히스토리 화면으로 진입");

        historyArrayList.add(new History("user1", "this is clipboard contents"));
        historyArrayList.add(new History("user2", "this is clipboard contents"));
        historyArrayList.add(new History("user3", "this is clipboard contents"));
        historyArrayList.add(new History("user4", "this is clipboard contents"));
        historyArrayList.add(new History("user5", "this is clipboard contents"));
        historyArrayList.add(new History("user6", "this is clipboard contents"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        historyAdapter = new HistoryAdapter(historyArrayList);
        recyclerView.setAdapter(historyAdapter);

        return view;
    }
}
