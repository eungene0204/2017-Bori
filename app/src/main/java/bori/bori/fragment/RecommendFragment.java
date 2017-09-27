package bori.bori.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.json.JSONObject;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import bori.bori.R;
import bori.bori.activity.MainActivity;
import bori.bori.activity.WebViewActivity;
import bori.bori.adapter.RecommendListAdapter;
import bori.bori.news.News;
import bori.bori.user.MyUser;
import bori.bori.volley.VolleyHelper;
import bori.bori.volley.VolleySingleton;


/**
 * Created by Eugene on 2017-03-07.
 */

public class RecommendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
    ,VolleyHelper.OnVolleyHelperListener
{
    public static String TAG = "RecommendFragment";

    private RecyclerView mRecyclerView;
    private RecommendListAdapter mAdapter;
    private OnRecommendFragmentListener mCallback;
    private List<News> mDataset;
    private MyUser mMyUser;

    private VolleyHelper mVolleyHelper;
    private ProgressDialog mProgressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<News> mNewsArrayList;

    private boolean mIsRefresh = false;
    private int mFontSize;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initDataSet();
        mProgressDialog = new ProgressDialog(getActivity());
        RequestQueue requestQueue =
                VolleySingleton.getInstance
                        (getActivity().getApplicationContext()).getRequestQueue();

        mVolleyHelper = new VolleyHelper(requestQueue,
                (AppCompatActivity)getActivity(),mProgressDialog);
        mVolleyHelper.setOnVolleyHelperListener(this);

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            mFontSize = bundle.getInt(MainActivity.KEY_FONT_SIZE);
        }
        else
        {
            mFontSize = (int)getResources().getDimension(R.dimen.webview_text_size_middle);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_recommend, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorAccent));

        mVolleyHelper.setSwipeRefreshLayout(mSwipeRefreshLayout);

        setRecyclerView(rootView);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mMyUser = mCallback.onRecommendFragmentCall();

        int count = mAdapter.getItemCount();

        if( mMyUser != null && (count == 0))
        {
            readNews();
        }

        return rootView;
    }

    private void setRecyclerView(View rootView)
    {

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(getActivity().getApplication());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecommendListAdapter(getActivity().getApplicationContext(),mDataset);
        mAdapter.setFontSize(mFontSize);
        mAdapter.setNewsClickListener((RecommendListAdapter.OnNewsClickListener) getActivity());
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new
                DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL);

        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.divider));

        mRecyclerView.addItemDecoration(dividerItemDecoration);
        setItemTouchHelper();

    }

    private void setItemTouchHelper()
    {

    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            mCallback = (OnRecommendFragmentListener) context;

        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + "must implement OnRecommendFragmentLister");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mCallback= null;
    }

    @Override
    public void onRefresh()
    {
        mIsRefresh = true;
        readNews();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setTitle(R.string.bori_news);

    }

    private void readNews()
    {
        Log.i(TAG, "send news request");
        JSONObject jsonObject = mVolleyHelper.writeJSON(mMyUser);
        JsonObjectRequest jsonObjectRequest = mVolleyHelper.jsonRequest(jsonObject);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);

        if(false == mIsRefresh)
        {
            mProgressDialog.setMessage("맞춤뉴스를 검색 중 입니다.");
            mProgressDialog.show();
        }

    }


    private void initDataSet()
    {
        mDataset = new ArrayList<News>();
    }

    public void setDataSet(ArrayList<News> newsArrayList)
    {
        Log.i(TAG,"setDataset");
        ArrayList<News> newsList = newsArrayList;
        mDataset.clear();

        for(int i = 0; i<newsList.size(); i++)
        {
            News news = newsList.get(i);
            mDataset.add(news);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNewsListener(ArrayList<News> newsArrayList)
    {
        mNewsArrayList = newsArrayList;
        setDataSet(newsArrayList);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnRecommendFragmentListener
    {
        public MyUser onRecommendFragmentCall();
    }


}
