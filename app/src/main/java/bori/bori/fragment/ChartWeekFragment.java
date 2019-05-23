package bori.bori.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import bori.bori.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartWeekFragment extends Fragment
{

    private BarChart mNewsChart;
    private BarChart mSaveChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_chart_week, container,false);

        mNewsChart = view.findViewById(R.id.chart_news);
        mSaveChart = view.findViewById(R.id.chart_save);

        setNewsChart();
        setSavedChart();

        return view;
    }

    private void setSavedChart()
    {
        mSaveChart.setDrawBarShadow(false);
        mSaveChart.getDescription().setEnabled(false);
        mSaveChart.setDrawValueAboveBar(true);
        mSaveChart.setPinchZoom(false);
        mSaveChart.setDoubleTapToZoomEnabled(false);
        mSaveChart.setDrawGridBackground(false);
        mSaveChart.getAxisRight().setDrawGridLines(false);
        mSaveChart.getAxisRight().setDrawLabels(false);
        mSaveChart.getAxisRight().setDrawAxisLine(false);
        mSaveChart.getLegend().setEnabled(false);
        mSaveChart.setScaleEnabled(false);
        mSaveChart.setExtraBottomOffset(10f);

        XAxis xAxis = mSaveChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(15f);
        xAxis.setTextColor(getActivity().getColor(R.color.GreyPrimary));
        xAxis.setDrawLabels(true);
        xAxis.setXOffset(10f);

        setLables(xAxis);

        YAxis yAxis = mSaveChart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        yAxis.setAxisMinimum(0f);

        setNewsChartData();

        mSaveChart.setFitBars(true);
        mSaveChart.animateXY(2500, 2500);
    }

    private void setNewsChart()
    {
        mNewsChart.setDrawBarShadow(false);
        mNewsChart.getDescription().setEnabled(false);
        mNewsChart.setDrawValueAboveBar(true);
        mNewsChart.setPinchZoom(false);
        mNewsChart.setDoubleTapToZoomEnabled(false);
        mNewsChart.setDrawGridBackground(false);
        mNewsChart.getAxisRight().setDrawGridLines(false);
        mNewsChart.getAxisRight().setDrawLabels(false);
        mNewsChart.getAxisRight().setDrawAxisLine(false);
        mNewsChart.getLegend().setEnabled(false);
        mNewsChart.setScaleEnabled(false);
        mNewsChart.setExtraBottomOffset(10f);

        XAxis xAxis = mNewsChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(15f);
        xAxis.setTextColor(getActivity().getColor(R.color.GreyPrimary));
        xAxis.setDrawLabels(true);
        xAxis.setXOffset(20f);

        setLables(xAxis);

        YAxis yAxis = mNewsChart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        yAxis.setAxisMinimum(0f);

        setNewsChartData();

        mNewsChart.setFitBars(true);
        mNewsChart.animateXY(2500, 2500);
    }

    private void setLables(XAxis xAxis)
    {
        String[] lebels = new String[5];
        lebels[0] = "문재인";
        lebels[1] = "이응준";
        lebels[2] = "신해철";
        lebels[3] = "박찬호";
        lebels[4] = "익스트림";
        /*
        lebels[5] = "군주론";
        lebels[6] = "치킨너겟";
        lebels[7] = "브레인신";
        lebels[8] = "스타크";
        lebels[9] = "백경순"; */

        xAxis.setLabelCount(lebels.length);
        xAxis.setValueFormatter(new XAxisValueFormatter(lebels));
    }

    private void setNewsChartData()
    {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 17f));
        entries.add(new BarEntry(1f, 45f));
        entries.add(new BarEntry(2f, 35f));
        entries.add(new BarEntry(3f, 97f));
        entries.add(new BarEntry(4f, 93f));
        /*
        entries.add(new BarEntry(5f, 27f));
        entries.add(new BarEntry(6f, 65f));
        entries.add(new BarEntry(7f, 35f));
        entries.add(new BarEntry(8f, 54f));
        entries.add(new BarEntry(9f, 73f)); */

        BarDataSet barDataSet = new BarDataSet(entries, "data");
        barDataSet.setColors(getActivity().getColor(R.color.bar_color));

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.6f);
        mNewsChart.setData(data);
        mSaveChart.setData(data);

    }

    public class XAxisValueFormatter extends ValueFormatter
    {
        private String[] mLabels;

        public XAxisValueFormatter(String[] labels)
        {
            this.mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value)
        {
            return this.mLabels[(int)value];
        }
    }
}
