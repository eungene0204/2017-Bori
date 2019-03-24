package bori.bori.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
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
import bori.bori.adapter.RealmFavNewsAdapter;
import bori.bori.adapter.RecommendListAdapter;
import bori.bori.realm.FavNews;
import bori.bori.realm.RealmController;
import bori.bori.utility.FontUtils;
import bori.bori.utility.SwipeUtil;
import io.realm.RealmResults;

/**
 * Created by Eugene on 2017-09-02.
 */

public class FavNewsFragment extends Fragment
{
    private static String TAG = FavNewsFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private FavNewsAdapter mAdapter;

    private int mFontSize;

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

        setupRecycler(rootView);
        setRealmAdapter(RealmController.with(getActivity()).getFavNewsAll());
        setSwipeForRecyclerVIew();

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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fav_news_recycler);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new
                LinearLayoutManager(getActivity().getApplication());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new FavNewsAdapter(getActivity());
        mAdapter.setFontSize(mFontSize);
        mAdapter.setNewsClickListener((RecommendListAdapter.OnNewsClickListener) getActivity());
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new
                DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL);

        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.divider));

        mRecyclerView.addItemDecoration(dividerItemDecoration);


    }

    private void setRealmAdapter(RealmResults<FavNews> favNews)
    {
        RealmFavNewsAdapter realmAdapter = new
                RealmFavNewsAdapter(this.getActivity().getApplicationContext(),
                favNews,true);

        mAdapter.setRealmBaseAdapter(realmAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private void setSwipeForRecyclerVIew()
    {
        SwipeUtil swipeHelper = new SwipeUtil(0, ItemTouchHelper.LEFT, getActivity())
        {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                int swipedPosition = viewHolder.getAdapterPosition();
                FavNewsAdapter adapter = (FavNewsAdapter) mRecyclerView.getAdapter();
                adapter.pendingRemoval(swipedPosition);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
            {
                int position = viewHolder.getAdapterPosition();
                FavNewsAdapter adapter = (FavNewsAdapter) mRecyclerView.getAdapter();

                if(adapter.isPendingRemoval(position))
                {
                    return 0;
                }

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

    }
}
