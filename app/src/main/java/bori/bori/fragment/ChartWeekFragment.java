package bori.bori.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import bori.bori.R;
import bori.bori.chart.ChartHelper;
import bori.bori.realm.RealmHelper;
import com.github.mikephil.charting.charts.BarChart;

public class ChartWeekFragment extends Fragment
{

    private BarChart mTodayNewsChart;
    private BarChart mSavedNewsChart;

    private RealmHelper mRealmHelper;
    private ChartHelper mChartHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_chart_week, container,false);

        mTodayNewsChart = view.findViewById(R.id.chart_news);
        mSavedNewsChart = view.findViewById(R.id.chart_save);

        mRealmHelper = new RealmHelper(getContext());
        mChartHelper = new ChartHelper(getContext());

        setTodayNewsChart();
        setSavedNewsChart();

        return view;
    }

    private void setTodayNewsChart()
    {
        String msg = getContext().getString(R.string.no_week_today_news);
        mChartHelper.setBarChart(mTodayNewsChart, RealmHelper.TYPE_WEEK_TODAY, msg);
    }

    private void setSavedNewsChart()
    {
        String msg = getContext().getString(R.string.no_week_saved_news);
        mChartHelper.setBarChart(mSavedNewsChart, RealmHelper.TYPE_WEEK_SAVED, msg);
    }

}
