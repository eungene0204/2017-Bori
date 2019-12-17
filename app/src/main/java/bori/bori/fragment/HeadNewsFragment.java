package bori.bori.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.google.android.material.appbar.AppBarLayout;
import org.json.JSONObject;


public class HeadNewsFragment extends Fragment implements
        VolleyHelper.OnVolleyHelperHeadNewsListener, SwipeRefreshLayout.OnRefreshListener

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

        mVolleyHelper.setHeadNewsListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_head_news, container, false);

        mSwipeRefreshLayout =  rootView.findViewById(R.id.head_news_swiperefresh);
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
        mRecyclerView = rootView.findViewById(R.id.list);
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(getActivity().getApplication());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new HeadNewsAdapter(getActivity().getApplicationContext(),mDataset,getFragmentManager());
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

        setAppBar();
    }

    private void setAppBar()
    {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView titleView = toolbar.findViewById(R.id.toolbar_main_tile);
        titleView.setText(getString(R.string.nav_head_news));

        AppBarLayout layout = (AppBarLayout) toolbar.getParent();
        layout.setExpanded(true,true);

    }


    private void readNews()
    {
        Log.i(TAG, "send news request");
        requestHeadNews();

    }

    private void requestHeadNews()
    {
        JSONObject jsonObject = JsonUtils.writeJSON(mMyUser);
        JsonObjectRequest jsonObjectRequest = mVolleyHelper.headNewsRequest(jsonObject,
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
