package bori.bori.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import bori.bori.news.NewsHelper;
import bori.bori.news.NewsInfo;
import bori.bori.realm.RealmHelper;
import bori.bori.utility.FontUtils;
import bori.bori.utility.JsonUtils;
import bori.bori.utility.SortUtils;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import io.realm.Sort;
import org.json.JSONObject;

import java.util.*;

import bori.bori.R;
import bori.bori.adapter.RecommendListAdapter;
import bori.bori.news.News;
import bori.bori.user.MyUser;
import bori.bori.volley.VolleyHelper;
import bori.bori.volley.VolleySingleton;


/**
 * Created by Eugene on 2017-03-07.
 */

public class RecommendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
    ,VolleyHelper.OnVolleyHelperRecommendNewsListener, NewsHelper.OnSortListener
{
    public static final String TAG = "RecommendFragment";

    public static final String TYPE_NEW_UPDATE = "new_update";
    public static final String TYPE_LOAD_MORE = "load_more";

    private RecyclerView mRecyclerView;
    private RecommendListAdapter mAdapter;
    private OnRecommendFragmentListener mListener;
    private List<News> mNewsList;
    private List<News> mMoreNewsList;
    private MyUser mMyUser;

    private VolleyHelper mVolleyHelper;
    private ProgressDialog mProgressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsRefresh = false;
    private int mFontSize;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //RealmHelper realmHelper = new RealmHelper(getContext());
        //realmHelper.deleteAll();

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
        mVolleyHelper.setmRecommendNewsListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_recommend, container, false);

        setToolbar();

        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorAccent));

        mVolleyHelper.setSwipeRefreshLayout(mSwipeRefreshLayout);

        setRecyclerView(rootView);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mMyUser = mListener.onRecommendFragmentCall();


        //getNewsInfo();

        int count = mAdapter.getItemCount();
        if( mMyUser != null && (count == 0))
        {
            readNews();
        }


        return rootView;
    }

    private void setToolbar()
    {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ImageView sortImg = toolbar.findViewById(R.id.sort_img);
        sortImg.setVisibility(View.VISIBLE);
    }

    private void getNewsInfo()
    {
        SharedPreferences prfs = getActivity().getPreferences(Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prfs.getString(NewsInfo.TAG,"");
        NewsInfo newsInfo = gson.fromJson(json,NewsInfo.class);

        if(newsInfo != null)
        {
            setDataSet(newsInfo);
        }
    }


    private void setRecyclerView(View rootView)
    {
        mRecyclerView =  rootView.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new
                LinearLayoutManager(getActivity().getApplication());

        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecommendListAdapter(getActivity().getApplicationContext(),mNewsList,
                getActivity().getSupportFragmentManager(),mRecyclerView);
        mAdapter.setFontSize(mFontSize);
        mAdapter.setNewsClickListener((RecommendListAdapter.OnNewsClickListener) getActivity());
        mAdapter.setOnLoadMoreListener(new RecommendListAdapter.OnLoadMoreListener()
        {
            @Override
            public void onLoadMore()
            {
                if(!mMoreNewsList.isEmpty())
                {
                    loadMore();
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadMore()
    {
        mNewsList.add(null);
        mAdapter.notifyItemInserted(mNewsList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mNewsList.remove(mNewsList.size() - 1);
                mAdapter.notifyItemRemoved(mNewsList.size());

                addMoreNews();

                mAdapter.notifyDataSetChanged();
                mAdapter.setLoaded();

            }
        },2000);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            mListener = (OnRecommendFragmentListener) context;

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
        mListener = null;
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

        setAppBar();


    }

    private void setAppBar()
    {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView titleView = toolbar.findViewById(R.id.toolbar_main_tile);
        titleView.setText(getString(R.string.nav_rcmd_news));

        AppBarLayout layout = (AppBarLayout) toolbar.getParent();
        layout.setExpanded(true,true);

    }

    private void readNews()
    {
        Log.i(TAG, "send news request");
        JSONObject jsonObject = JsonUtils.writeJSON(mMyUser);
        JsonObjectRequest jsonObjectRequest = mVolleyHelper.rcmdRequest(jsonObject,
                VolleyHelper.RCMD_NEWS_URL);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);

        if(false == mIsRefresh)
        {
            mProgressDialog.setMessage("맞춤뉴스를 검색 중 입니다.");
            mProgressDialog.show();
        }

    }


    private void initDataSet()
    {
        mNewsList= new ArrayList<News>();
        mMoreNewsList = new ArrayList<News>();
    }

    private void addMoreNews()
    {
        if(!mMoreNewsList.isEmpty())
        {
            int addSize = 15;

            try
            {
                for(int i = 0 ; i < addSize; i++)
                {
                    mNewsList.add(mMoreNewsList.get(i));
                    mMoreNewsList.remove(i);
                }

            }
            catch (IndexOutOfBoundsException e)
            {
                Log.e(TAG,String.valueOf(e));
            }

        }

    }

    private void setDataSet(NewsInfo newsInfo)
    {
        Log.i(TAG,"setDataset");
        ArrayList<News> newsList = (ArrayList<News>) newsInfo.getNewsList();

        mNewsList.clear();
        mMoreNewsList.clear();

        int originSize = newsList.size();
        int advanceListSize = 15;

        if(newsList.size() >= advanceListSize)
        {
            List temp = new ArrayList<News>(newsList.subList(0,advanceListSize));

            mNewsList.addAll(temp);

            mMoreNewsList.addAll(new ArrayList<News>(newsList.subList(advanceListSize, originSize)));
        }
        else
        {
            mNewsList.addAll(newsList);
        }


        Collections.shuffle(mNewsList);
        Collections.shuffle(mMoreNewsList);

        mAdapter.notifyDataSetChanged();
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

    @Override
    public void onNewsUpdate(NewsInfo newsInfo)
    {
        setDataSet(newsInfo);
    }

    @Override
    public void onSort(String sortType)
    {
        Comparator<News> cmp = null;

        if(sortType.equals(SortUtils.TYPE_SIM))
        {
            cmp = new Comparator<News>()
            {
                @Override
                public int compare(News o1, News o2)
                {
                    return (o1.getSimilarityLevel().compareTo(o2.getSimilarityLevel()));

                }
            };
        }
        else if(sortType.equals(SortUtils.TYPE_SNS))
        {
            cmp = new Comparator<News>()
            {
                @Override
                public int compare(News o1, News o2)
                {
                    return (o1.getOriginal().compareTo(o2.getOriginal()));

                }
            };

        }

        Collections.sort(mNewsList, cmp);
        mAdapter.notifyDataSetChanged();

    }

    public interface OnRecommendFragmentListener
    {
        MyUser onRecommendFragmentCall();
    }


}
