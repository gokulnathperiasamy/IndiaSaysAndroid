package com.surveyin.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.surveyin.R;
import com.surveyin.application.ApplicationConstant;
import com.surveyin.application.EndPoint;
import com.surveyin.entity.QuestionResult;
import com.surveyin.entity.Result;
import com.surveyin.utility.NetworkUtil;
import com.synnapps.carouselview.ViewListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class ResultActivity extends BaseActivity {

    @Bind(R.id.avi_loading)
    AVLoadingIndicatorView mAVILoading;

    @Bind(R.id.loading_message)
    TextView mLoadingMessage;

    @Bind(R.id.loading_error_image)
    ImageView mLoadingErrorImage;

    @Bind(R.id.retry_connection)
    ImageView mRetryConnection;

    List<QuestionResult> questionResultList;

    protected String[] mParties = new String[] {
            "Option A", "Option B", "Option C", "Option D"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        getResultData();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.retry_connection)
    public void retryConnection(View view) {
        mLoadingErrorImage.setVisibility(View.GONE);
        mAVILoading.setVisibility(View.VISIBLE);
        mLoadingMessage.setText(getString(R.string.loading_message));
        mRetryConnection.setVisibility(View.GONE);
        checkNetworkAndLoadData();
    }

    private void getResultData() {
        checkNetworkAndLoadData();
    }

    /**************************** UI Updates *********************************/

    private void updateErrorUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showErrorLayout(true);
            }
        });
    }

    private void showErrorLayout(boolean isNetworkAvailable) {
        mAVILoading.setVisibility(View.GONE);
        mLoadingErrorImage.setVisibility(View.VISIBLE);
        mRetryConnection.setVisibility(View.VISIBLE);
        if (isNetworkAvailable) {
            mLoadingMessage.setText(getString(R.string.error_loading_message));
        } else {
            mLoadingMessage.setText(getString(R.string.network_error_message));
        }
    }

    private void updateResultUI(final boolean isResultsAvailable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showResultsLayout(isResultsAvailable);
            }
        });
    }

    private void showResultsLayout(boolean isResultsAvailable) {
        if (isResultsAvailable) {
            mAVILoading.setVisibility(View.GONE);
            mLoadingErrorImage.setVisibility(View.GONE);
            mRetryConnection.setVisibility(View.GONE);
        }
    }

    /*************************** Network Calls *******************************/

    private void checkNetworkAndLoadData() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            loadData();
        } else {
            showErrorLayout(false);
        }
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIsConnectedToServer();
            }
        }, ApplicationConstant.DELAY_LOADING);
    }

    private void checkIsConnectedToServer() {
        Request request = new Request.Builder()
                .url(EndPoint.IS_CONNECTED)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                parseServerConnectionCheckResponse(null);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                parseServerConnectionCheckResponse(response);
            }
        });
    }

    private void loadResultsFromServer() {
        Request request = new Request.Builder()
                .url(EndPoint.GET_ALL_QUESTION_RESULTS)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                parseResultsResponse(null);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                parseResultsResponse(response);
            }
        });
    }

    /************************** Parse Response *******************************/

    private void parseServerConnectionCheckResponse(Response response) {
        if (response != null && response.isSuccessful()) {
            loadResultsFromServer();
        } else {
            showErrorLayout(false);
        }
    }

    private void parseResultsResponse(Response response) {
        questionResultList = new ArrayList<>();
        if (response != null && response.isSuccessful() && response.body() != null) {
            try {
                String responseString = response.body().string();
                JSONArray jsonArrayResponse = new JSONArray(responseString);

                for (int i = 0; i < jsonArrayResponse.length(); i++) {
                    JSONObject jsonObjectQuestion = jsonArrayResponse.getJSONObject(i);
                    QuestionResult questionResult = new QuestionResult();
                    questionResult.question = jsonObjectQuestion.getString(ApplicationConstant.QUESTION);
                    questionResult.optionA = jsonObjectQuestion.getString(ApplicationConstant.OPTION_A);
                    questionResult.optionB = jsonObjectQuestion.getString(ApplicationConstant.OPTION_B);
                    questionResult.optionC = jsonObjectQuestion.getString(ApplicationConstant.OPTION_C);
                    questionResult.optionD = jsonObjectQuestion.getString(ApplicationConstant.OPTION_D);

                    JSONArray jsonArrayResult = jsonObjectQuestion.getJSONArray(ApplicationConstant.RESULT);
                    List<Result> questionResults = new ArrayList<>();
                    for (int j = 0; j < jsonArrayResult.length(); j++) {
                        JSONObject jsonObjectQuestionResults = jsonArrayResult.getJSONObject(j);
                        Result results = new Result();
                        results.questionID = jsonObjectQuestionResults.getString(ApplicationConstant.QUESTION_ID);
                        results.question = jsonObjectQuestionResults.getString(ApplicationConstant.QUESTION);
                        results.gender = jsonObjectQuestionResults.getString(ApplicationConstant.GENDER);
                        results.options = jsonObjectQuestionResults.getString(ApplicationConstant.OPTIONS);
                        results.ageGroup_00_14 = jsonObjectQuestionResults.getLong(ApplicationConstant.AGE_GROUP_00_14_RESPONSE);
                        results.ageGroup_15_24 = jsonObjectQuestionResults.getLong(ApplicationConstant.AGE_GROUP_15_24_RESPONSE);
                        results.ageGroup_25_34 = jsonObjectQuestionResults.getLong(ApplicationConstant.AGE_GROUP_25_34_RESPONSE);
                        results.ageGroup_35_44 = jsonObjectQuestionResults.getLong(ApplicationConstant.AGE_GROUP_35_44_RESPONSE);
                        results.ageGroup_45_99 = jsonObjectQuestionResults.getLong(ApplicationConstant.AGE_GROUP_45_99_RESPONSE);
                        questionResults.add(results);
                    }
                    questionResult.result = questionResults;
                    questionResultList.add(questionResult);
                    updateResultUI(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            updateResultUI(false);
        }
    }

    /***************************** Pie Chart *********************************/

    private void setupPieData(PieChart pieChart, String lable) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterText(lable);
        pieChart.setCenterTextSize(14);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(50f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        // pieChart.setRotationEnabled(true);
        // pieChart.setHighlightPerTapEnabled(true);

        setData(pieChart, 4, 100);

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
    }

    private void setData(PieChart pieChart, int count, float range) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * range) + range / count), mParties[i], null));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }
}
