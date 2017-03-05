package com.capstone.tip.emassage.ui.grades.detail;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ActivityGradesDetailBinding;
import com.capstone.tip.emassage.model.data.Grade;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

public class GradesDetailActivity extends MvpActivity<GradesDetailView, GradesDetailPresenter> implements GradesDetailView {

    private ActivityGradesDetailBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grades_detail);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int lessonId = getIntent().getIntExtra("lesson", -1);

        binding.lineChart.setDrawGridBackground(false);
        binding.lineChart.getDescription().setEnabled(false);
        binding.lineChart.setTouchEnabled(false);
        binding.lineChart.setDragEnabled(false);
        binding.lineChart.setScaleEnabled(false);
        binding.lineChart.setPinchZoom(false);

        XAxis xAxis = binding.lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = binding.lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(100f);
        leftAxis.setSpaceTop(10f);
        leftAxis.setAxisMinimum(50f);
        leftAxis.setLabelCount(6, true);
        leftAxis.setYOffset(20f);
        binding.lineChart.getAxisRight().setEnabled(false);
        Legend l = binding.lineChart.getLegend();
        l.setForm(Legend.LegendForm.SQUARE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        presenter.start(lessonId);
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveGrades();
            }
        });
    }

    @Override
    protected void onDestroy() {
        presenter.stop();
        super.onDestroy();
    }

    @NonNull
    @Override
    public GradesDetailPresenter createPresenter() {
        return new GradesDetailPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setData(ArrayList<Entry> values) {
        LineDataSet set1;
        if (binding.lineChart.getData() != null &&
                binding.lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) binding.lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            binding.lineChart.getData().notifyDataChanged();
            binding.lineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Average per number of takes");

            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(false);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            binding.lineChart.setData(data);
        }
    }

    @Override
    public void setGradesList(List<Grade> grades) {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < grades.size(); i++) {
            float val = grades.get(i).averageFloat();
            values.add(new Entry(i + 1, val, getResources().getDrawable(R.drawable.star)));
        }
        if (values.size() > 0) {
            binding.lineChart.getXAxis().setLabelCount(grades.size(), true);
            setData(values);
        }
    }

    @Override
    public void setLessonTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(title);
        }
    }

    @Override
    public void setFormattedAverage(String formattedAverage) {
        binding.txtTotalAve.setText(formattedAverage);
    }

    @Override
    public void setNeedToSave(boolean needToSave) {
        binding.txtOnlineStatus.setText(needToSave ? "Need to Save" : "Nothing to Saved");
        binding.btnSave.setVisibility(needToSave ? View.VISIBLE : View.GONE);
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isShowing()) progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
