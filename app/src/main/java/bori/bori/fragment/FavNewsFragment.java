package bori.bori.fragment;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bori.bori.R;
import bori.bori.adapter.FavNewsAdapter;
import bori.bori.realm.FavNews;
import bori.bori.realm.RealmHelper;
import bori.bori.utility.FontUtils;
import bori.bori.utility.SwipeUtil;
import com.google.android.material.appbar.AppBarLayout;
import io.realm.RealmResults;

/**
 * Created by Eugene on 2017-09-02.
 */

public class FavNewsFragment extends Fragment
{
    public static String TAG = FavNewsFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RealmHelper mRealmHelper;


    private int mFontSize;
    private FavNewsAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_fav_news, container, false);

        mRealmHelper = new RealmHelper(getContext());

        setupRecycler(rootView);

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            mFontSize = bundle.getInt(FontUtils.KEY_FONT_SIZE);
        }
        else
        {
            mFontSize = (int)getResources().getDimension(R.dimen.webview_text_size_middle);
        }


        return rootView;
    }


    private void setupRecycler(View rootView)
    {

        mRecyclerView = rootView.findViewById(R.id.fav_news_recycler);
        //mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new
                LinearLayoutManager(getActivity().getApplication());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);

        RealmResults<FavNews> results = getRealmResult();
        mAdapter = new FavNewsAdapter(results,getActivity(),getFragmentManager());
        mAdapter.setFontSize(mFontSize);
        //mAdapter.setNewsClickListener((RecommendListAdapter.OnNewsClickListener) getActivity());
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new
                DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL);

        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.divider));

        //mRecyclerView.addItemDecoration(dividerItemDecoration);


    }

    private RealmResults<FavNews> getRealmResult()
    {

        return mRealmHelper.getAllSavedNews();
    }

    private void setRealmAdapter(RealmResults<FavNews> favNews)
    {
        /*
        RealmFavNewsAdapter realmAdapter = new
                RealmFavNewsAdapter(this.getActivity().getApplicationContext(),
                favNews,true);

        mAdapter.setRealmBaseAdapter(realmAdapter);
        mAdapter.notifyDataSetChanged(); */

    }

    private void setSwipeForRecyclerVIew()
    {
        SwipeUtil swipeHelper = new SwipeUtil(0, ItemTouchHelper.LEFT, getActivity())
        {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                int swipedPosition = viewHolder.getAdapterPosition();
                //FavNewsAdapter adapter = (FavNewsAdapter) mRecyclerView.getAdapter();
                //adapter.pendingRemoval(swipedPosition);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
            {
                int position = viewHolder.getAdapterPosition();

                /*
                FavNewsAdapter adapter = (FavNewsAdapter) mRecyclerView.getAdapter();

                if(adapter.isPendingRemoval(position))
                {
                    return 0;
                } */

                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelper);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        swipeHelper.setLeftSwipeLable(getString(R.string.delete));
        swipeHelper.setLeftcolorCode(ContextCompat.getColor(getActivity(), R.color.swipebg));

    }

    @Override
    public void onResume()
    {
        super.onResume();

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setTitle(R.string.nav_fav);

        setAppBar();

    }

    private void setAppBar()
    {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView titleView = toolbar.findViewById(R.id.toolbar_main_tile);
        titleView.setText(getString(R.string.nav_fav));

        AppBarLayout layout = (AppBarLayout) toolbar.getParent();
        layout.setExpanded(true,true);

    }

}
