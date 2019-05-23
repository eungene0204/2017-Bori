package bori.bori.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import bori.bori.R;
import bori.bori.adapter.StatPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class StatFragment extends Fragment
{
    public static final String TAG = "StatPager";

    private ViewPager mViewPager;
    private StatPagerAdapter mAdapter;
    private TabLayout mTabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_stat,container,false);

        setToolbar();

        mViewPager = view.findViewById(R.id.stat_viewpager);
        mAdapter = new StatPagerAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(mAdapter);

        mTabLayout = view.findViewById(R.id.stat_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private void setToolbar()
    {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ImageView sortImg = toolbar.findViewById(R.id.sort_img);
        sortImg.setVisibility(View.GONE);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setAppBar();
    }

    private void setAppBar()
    {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView titleView = toolbar.findViewById(R.id.toolbar_main_tile);
        titleView.setText(getString(R.string.nav_stat));

        AppBarLayout layout = (AppBarLayout) toolbar.getParent();
        layout.setExpanded(true,true);

    }

}
