package bori.bori.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import bori.bori.R;
import bori.bori.chart.ChartHelper;
import bori.bori.realm.TodayCategory;
import bori.bori.realm.SavedCategory;
import bori.bori.realm.RealmHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.realm.implementation.RealmBarDataSet;
import io.realm.RealmResults;

public class ChartTodayFragment extends Fragment
{

    private BarChart mTodayNewsChart;
    private BarChart mSavedNewsChart;
    private RealmHelper mRealmHelper;
    private ChartHelper mChartHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_chart_today, container,false);

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
        String msg = getContext().getString(R.string.no_today_news);
        mChartHelper.setBarChart(mTodayNewsChart, RealmHelper.TYPE_TODAY, msg);
    }

    private void setSavedNewsChart()
    {
        String msg = getContext().getString(R.string.no_saved_news);
        mChartHelper.setBarChart(mSavedNewsChart, RealmHelper.TYPE_SAVE, msg);

    }

}
