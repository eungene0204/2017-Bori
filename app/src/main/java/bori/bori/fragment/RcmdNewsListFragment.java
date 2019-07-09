package bori.bori.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bori.bori.R;
import bori.bori.adapter.RecommendListAdapter;
import bori.bori.news.News;
import bori.bori.utility.FontUtils;

import java.util.List;

public class RcmdNewsListFragment extends Fragment
{
    public static final String TAG = "RcmdNewsListFragment";

    private RecyclerView mRecyclerView;
    private List<News> mNewsList;

    private int mFontSize;
    private RecommendListAdapter mAdapter;

    public RcmdNewsListFragment(List<News> newsList)
    {
        mNewsList = newsList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view  = inflater.inflate(R.layout.activity_remd_news_list, container, false);

        setupRecycler(view);

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            mFontSize = bundle.getInt(FontUtils.KEY_FONT_SIZE);
        }
        else
        {
            mFontSize = (int)getResources().getDimension(R.dimen.webview_text_size_middle);
        }


        return view;
    }

     private void setupRecycler(View rootView)
    {

        mRecyclerView = rootView.findViewById(R.id.news_recycler);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new
                LinearLayoutManager(getActivity().getApplication());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecommendListAdapter(getContext(),mNewsList,getFragmentManager(),mRecyclerView);

        mAdapter.setFontSize(mFontSize);
        //mAdapter.setNewsClickListener((RecommendListAdapter.OnNewsClickListener) getActivity());
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new
                DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL);

        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.divider));

        mRecyclerView.addItemDecoration(dividerItemDecoration);

    }

}
