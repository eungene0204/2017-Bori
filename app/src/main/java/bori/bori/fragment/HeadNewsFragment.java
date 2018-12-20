package bori.bori.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import bori.bori.R;
import bori.bori.adapter.HeadNewsAdapter;
import bori.bori.news.News;
import bori.bori.news.NewsInfo;
import bori.bori.user.MyUser;
import bori.bori.utility.FontUtils;
import bori.bori.utility.JsonUtils;
import bori.bori.volley.VolleyHelper;
import bori.bori.volley.VolleySingleton;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;


public class HeadNewsFragment extends Fragment implements VolleyHelper.OnVolleyHelperHeadNewsListener, SwipeRefreshLayout.OnRefreshListener
{
    public static String TAG = HeadNewsFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private HeadNewsAdapter mAdapter;
    private OnHeadNewsFragmentInteractionListener mListener;
    private List<News> mDataset;
    private MyUser mMyUser;

    private VolleyHelper mVolleyHelper;
    private ProgressDialog mProgressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<News> mNewsArrayList;

    private boolean mIsRefresh = false;
    private int mFontSize;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initDataSet();
        mProgressDialog = new ProgressDialog(getActivity());

        initVolley();

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            mFontSize = bundle.getInt(FontUtils.KEY_FONT_SIZE);
        }
        else
        {
            mFontSize = (int)getResources().getDimension(R.dimen.webview_text_size_middle);
        }
    }

    private void initVolley()
    {

        RequestQueue requestQueue =
                VolleySingleton.getInstance
                        (getActivity().getApplicationContext()).getRequestQueue();
        mVolleyHelper = new VolleyHelper(requestQueue,
                (AppCompatActivity)getActivity(),mProgressDialog);

        mVolleyHelper.setmHeadNewsListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_head_news, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.head_news_swiperefresh);
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorAccent));

        mVolleyHelper.setSwipeRefreshLayout(mSwipeRefreshLayout);

         setRecyclerView(rootView);

         mSwipeRefreshLayout.setOnRefreshListener(this);

         mMyUser = mListener.onHeadNewsFragmentCall();



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

        mAdapter = new HeadNewsAdapter(getActivity().getApplicationContext(),mDataset);
        mAdapter.setFontSize(mFontSize);
        mAdapter.setNewsClickListener((HeadNewsAdapter.OnHeadNewsClickListener) getActivity());
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new
                DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL);

        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.divider));

        mRecyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHeadNewsFragmentInteractionListener)
        {
            mListener = (OnHeadNewsFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnHeadNewsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setTitle(R.string.nav_head_news);

    }

    private void readNews()
    {
        Log.i(TAG, "send news request");

        requestHeadNews();
        requestRcmdNews();

    }

    private void requestHeadNews()
    {
        JSONObject jsonObject = JsonUtils.writeJSON(mMyUser);
        JsonObjectRequest jsonObjectRequest = mVolleyHelper.jsonRequest(jsonObject,
                VolleyHelper.HEAD_NEWS_URL);

        jsonObjectRequest.setShouldCache(false);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);

        if(false == mIsRefresh)
        {
            String msg = getResources().getString(R.string.dlg_head_news);
            mProgressDialog.setMessage(msg);
            mProgressDialog.show();
        }


    }

    private void requestRcmdNews()
    {

        JSONObject jsonObject = JsonUtils.writeJSON(mMyUser);
        JsonObjectRequest jsonObjectRequest = mVolleyHelper.rcmdRequest(jsonObject,
                VolleyHelper.RCMD_NEWS_URL);

        jsonObjectRequest.setShouldCache(false);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void initDataSet()
    {
        mDataset = new ArrayList<News>();
    }

    public void setDataSet(NewsInfo newsInfo)
    {
        Log.i(TAG,"setDataset");
        ArrayList<News> newsList = (ArrayList<News>) newsInfo.getNewsList();
        mDataset.clear();

        for(News news : newsList)
        {
            mDataset.add(news);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh()
    {
        mIsRefresh = true;
        readNews();
    }

    @Override
    public void onNewsUpdate(NewsInfo newsInfo)
    {
        setDataSet(newsInfo);

    }

    public interface OnHeadNewsFragmentInteractionListener
    {
        MyUser onHeadNewsFragmentCall();
    }


}
