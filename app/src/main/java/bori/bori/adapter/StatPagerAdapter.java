package bori.bori.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import bori.bori.R;
import bori.bori.fragment.ChartTodayFragment;
import bori.bori.fragment.ChartWeekFragment;

public class StatPagerAdapter extends FragmentPagerAdapter
{

    private final int FragmtCount = 2;

    private final Activity mActivity;

    public StatPagerAdapter(@NonNull FragmentManager fm, Activity activity)
    {
        super(fm);
        mActivity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new ChartTodayFragment();
            case 1:
                return new ChartWeekFragment();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return FragmtCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        String title ="default";

        switch (position)
        {
            case 0:
                return title = mActivity.getResources().getString(R.string.tab_today);
            case 1:
                return title = mActivity.getResources().getString(R.string.tab_week);
        }

        return title;
    }
}
