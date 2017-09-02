package com.surveyin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.surveyin.R;
import com.surveyin.activity.ResultActivity;
import com.surveyin.entity.QuestionResult;

import java.util.List;

public class ResultListAdapter extends RecyclerView.Adapter<ResultListViewHolder> {

    List<QuestionResult> questionResultList;
    Context context;

    public ResultListAdapter(List<QuestionResult> questionResultList, Context context) {
        this.questionResultList = questionResultList;
        this.context = context;
    }

    @Override
    public ResultListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_result_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ResultListViewHolder holder, int position) {
        holder.bindView(questionResultList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return questionResultList.size();
    }
}
