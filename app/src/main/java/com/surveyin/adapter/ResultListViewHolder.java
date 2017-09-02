package com.surveyin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.surveyin.R;
import com.surveyin.entity.QuestionResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ResultListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.tv_question)
    TextView mQuestion;

    QuestionResult questionResult;
    Context context;

    public ResultListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(QuestionResult questionResult, Context context) {
        this.questionResult = questionResult;
        this.context = context;
        mQuestion.setText(questionResult.question);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.view_question_result)
    public void viewQuestionResult(View view) {
        Toast.makeText(context.getApplicationContext(), questionResult.optionA, Toast.LENGTH_SHORT).show();
    }
}
