package bori.bori.chart;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import bori.bori.R;
import bori.bori.realm.RealmHelper;
import bori.bori.realm.SavedCategory;
import bori.bori.realm.TodayCategory;
import bori.bori.utility.TimeUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.realm.implementation.RealmBarDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ChartHelper
{
    private Context mContext;
    private RealmHelper mRealmHelper;

    public ChartHelper(Context context)
    {
        mContext = context;
        mRealmHelper = new RealmHelper(mContext);
    }

    public void setBarChart(BarChart chart, String type, String emptyMsg)
    {
        chart.setDrawBarShadow(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(true);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setDrawGridBackground(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getLegend().setEnabled(false);
        chart.setScaleEnabled(false);
        chart.setExtraBottomOffset(10f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(15f);
        xAxis.setTextColor(mContext.getColor(R.color.GreyPrimary));
        xAxis.setDrawLabels(true);
        xAxis.setXOffset(10f);
        xAxis.setGranularityEnabled(true);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        yAxis.setAxisMinimum(0f);

        setChartData(chart, type, emptyMsg);

        chart.setFitBars(true);
        chart.animateXY(2500, 2500);

    }

    private void setChartData(BarChart chart,String type, String emptyMsg)
    {
        if(type.equals(RealmHelper.TYPE_TODAY) || type.equals(RealmHelper.TYPE_WEEK_TODAY))
        {
            RealmResults<TodayCategory> results = mRealmHelper.getCategory(type);
            if(results.isEmpty())
            {
                chart.setNoDataText(emptyMsg);
                return;
            }

            RealmBarDataSet<TodayCategory> dataSet = new
                    RealmBarDataSet<>(results,null, "mValue");

            dataSet.setColors(mContext.getColor(R.color.bar_color));
            dataSet.setValueFormatter(new YAxisValueFormatter());

            BarData data = new BarData(dataSet);
            data.setValueTextSize(15f);
            data.setValueTextColor(ContextCompat.getColor(mContext,R.color.GreyPrimary));
            data.setBarWidth(0.6f);

            setTodayChartLabels(results, chart.getXAxis());
            setTodayNewsChartSize(chart, results.size());

            chart.setData(data);

        }
        else if(type.equals(RealmHelper.TYPE_SAVE) || type.equals(RealmHelper.TYPE_WEEK_SAVED))
        {
            RealmResults<SavedCategory> results = mRealmHelper.getCategory(type);
            if(results.isEmpty())
            {
                chart.setNoDataText(emptyMsg);
                return;
            }

            RealmBarDataSet<SavedCategory> dataSet = new
                    RealmBarDataSet<>(results,null, "mValue");

            dataSet.setColors(mContext.getColor(R.color.bar_color));
            dataSet.setValueFormatter(new YAxisValueFormatter());

            BarData data = new BarData(dataSet);
            data.setValueTextSize(15f);
            data.setValueTextColor(ContextCompat.getColor(mContext,R.color.GreyPrimary));
            data.setBarWidth(0.6f);

            setSavedChartLabels(results, chart.getXAxis());
            setSavedNewsChartSize(chart, results.size());

            chart.setData(data);

        }

    }


    public void setTodayChartLabels(RealmResults<TodayCategory> results, XAxis xAxis)
    {
        if(results.isEmpty())
            return;

        int size = results.size();
        String[] labels = new String[size];

        for(int i = 0; i < size; i++)
        {
            TodayCategory category = results.get(i);
            String label = category.getCategory();

            labels[i] = label;
        }

        xAxis.setLabelCount(labels.length);
        xAxis.setValueFormatter(new ChartHelper.XAxisValueFormatter(labels));
    }

    public void setSavedChartLabels(RealmResults<SavedCategory> results, XAxis xAxis)
    {
         if(results.isEmpty())
            return;

        int size = results.size();
        String[] labels = new String[size];

        for(int i = 0; i < size; i++)
        {
            SavedCategory category = results.get(i);
            String label = category.getCategory();

            labels[i] = label;
        }

        xAxis.setLabelCount(labels.length);
        xAxis.setValueFormatter(new ChartHelper.XAxisValueFormatter(labels));
    }



    public void setTodayNewsChartSize(BarChart chart, int size)
    {

        ViewGroup.LayoutParams params = chart.getLayoutParams();
        int pixel = 100 * size;

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                pixel, mContext.getResources().getDisplayMetrics());

        params.width = width;

        chart.setLayoutParams(params);

    }

    public void setSavedNewsChartSize(BarChart chart, int size)
    {

        ViewGroup.LayoutParams params = chart.getLayoutParams();
        int pixel = 60 * size;

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                pixel, mContext.getResources().getDisplayMetrics());

        params.height = height;

        chart.setLayoutParams(params);

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

    public class YAxisValueFormatter extends ValueFormatter
    {

        public YAxisValueFormatter()
        {
        }

        @Override
        public String getFormattedValue(float value)
        {
            String val = String.valueOf((int) value) + "개";

            return val;
        }
    }


}
