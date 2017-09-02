package com.surveyin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.surveyin.R;
import com.surveyin.entity.QuestionResult;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ResultListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.tv_question)
    TextView mQuestion;

    public ResultListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(QuestionResult questionResult) {
        mQuestion.setText(questionResult.question);
    }
}
