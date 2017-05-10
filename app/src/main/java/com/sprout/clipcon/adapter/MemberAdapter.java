package com.sprout.clipcon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sprout.clipcon.R;
import com.sprout.clipcon.model.Member;

import java.util.ArrayList;

/**
 * Created by Yongwon on 2017. 4. 30..
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder>{

    private ArrayList<Member> memberList;

    public MemberAdapter(ArrayList<Member> membersArrayList) {
        memberList = membersArrayList;
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member, null);
        MemberViewHolder memberViewHolder = new MemberViewHolder(view);

        return memberViewHolder;
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        Member member = memberList.get(position);
        holder.nickView.setText(member.getNickname());

    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {

        TextView nickView;

        public MemberViewHolder(final View memberView) {
            super(memberView);

            nickView = (TextView)memberView.findViewById(R.id.nickName);
        }
    }
}
