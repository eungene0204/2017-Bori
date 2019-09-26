package bori.bori.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bori.bori.adapter.RecommendCardAdapter;
import bori.bori.databinding.FragmentRecommendCardBinding;
import bori.bori.databinding.RcmdNewsSubItemBinding;
import bori.bori.news.Category;
import bori.bori.news.NewsHelper;
import bori.bori.news.NewsInfo;
import bori.bori.utility.FontUtils;
import bori.bori.utility.JsonUtils;
import bori.bori.utility.SortUtils;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.*;

import bori.bori.R;
import bori.bori.news.News;
import bori.bori.user.MyUser;
import bori.bori.volley.VolleyHelper;
import bori.bori.volley.VolleySingleton;


/**
 * Created by Eugene on 2017-03-07.
 */

public class RecommendCardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
    ,VolleyHelper.OnVolleyHelperRecommendNewsListener, NewsHelper.OnSortListener
{
    public static final String TAG = "RecommendCardFragment";

    public static final String TYPE_NEW_UPDATE = "new_update";
    public static final String TYPE_LOAD_MORE = "load_more";

    private RecyclerView mRecyclerView;
    private RecommendCardAdapter mCardAdapter;
    private OnRecommendFragmentListener mListener;
    private List<Category> mCategoryList;
    private MyUser mMyUser;

    private VolleyHelper mVolleyHelper;
    private ProgressDialog mProgressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsRefresh = false;
    private int mFontSize;

    private FragmentRecommendCardBinding mBinding;

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
        mVolleyHelper.setRecommendNewsListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //View rootView = inflater.inflate(R.layout.fragment_recommend_card, container, false);
         mBinding = DataBindingUtil.inflate
                (inflater,R.layout.fragment_recommend_card,container,false);
        setToolbar();

        //mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout = mBinding.swiperefresh;
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorAccent));

        mVolleyHelper.setSwipeRefreshLayout(mSwipeRefreshLayout);


        setRecyclerView();

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mMyUser = mListener.onRecommendFragmentCall();


        //getNewsInfo();

        int count = mCardAdapter.getItemCount();
        if( mMyUser != null && (count == 0))
        {
            getNews();
        }


        return mBinding.getRoot();
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
            //setDataSet(newsInfo);
        }
    }


    private void setRecyclerView()
    {

        //mRecyclerView =  rootView.findViewById(R.id.card_list);

        mRecyclerView = mBinding.cardRecyclerview;
        //mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new
                LinearLayoutManager(getActivity().getApplication());

        mRecyclerView.setLayoutManager(layoutManager);

        //List<RcmdNewsSubItemBinding> subItemBindings = asyncInflate();

       mCardAdapter = new RecommendCardAdapter(getActivity().getSupportFragmentManager(),
                mCategoryList,getContext(), getActivity());


        mCardAdapter.setHasStableIds(true);

        mBinding.cardRecyclerview.setAdapter(mCardAdapter);
        //mBinding.setCardList(mCategoryList);

    }

    private List<RcmdNewsSubItemBinding> asyncInflate()
    {
        int size = 30;
        List<RcmdNewsSubItemBinding> subItemBindings = new ArrayList<>();

        for(int i = 0; i < size; i++)
        {
            AsyncLayoutInflater inflater = new AsyncLayoutInflater(getActivity().getApplicationContext());
            inflater.inflate(R.layout.rcmd_news_sub_item, null, new AsyncLayoutInflater.OnInflateFinishedListener()
            {
                @Override
                public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent)
                {

                    RcmdNewsSubItemBinding mRcmdNewsSubItemBinding = DataBindingUtil.bind(view);
                    subItemBindings.add(mRcmdNewsSubItemBinding);
                }
            });

        }

        return subItemBindings;

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
        getNews();
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

    private void getNews()
    {
        Log.i(TAG, "send news request");
        JSONObject jsonObject = JsonUtils.writeJSON(mMyUser);
        JsonObjectRequest jsonObjectRequest = mVolleyHelper.rcmdNewsRequest(jsonObject,
                VolleyHelper.RCMD_NEWS_URL);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);

        if(false == mIsRefresh)
        {
            mProgressDialog.setMessage("맞춤뉴스를 검색 중 입니다.");
            //mProgressDialog.show();
        }

    }


    private void initDataSet()
    {
        mCategoryList = new ArrayList<>();
    }


    private void setDataSet(NewsInfo newsInfo)
    {
        Log.i(TAG,"setDataset");
        ArrayList<News> newsList = (ArrayList<News>) newsInfo.getNewsList();
        for(News news : newsList)
        {
            if(!(mCategoryList.contains(news)))
            {
                Category category = new Category(news.getCategory());
                category.setLevel(news.getSimilarityLevel());
                category.addNews(news);

                mCategoryList.add(category);

            }
            else
            {
                for(Category category : mCategoryList)
                {
                    if(category.getCategory().equals(news.getCategory()))
                    {
                        category.addNews(news);
                        break;
                    }
                }
            }

        }

        //Collections.shuffle(mNewsList);

        mCardAdapter.notifyDataSetChanged();
        //mCardAdapter.initSubViewHolder(mCategoryList.size());
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

        //Collections.sort(mNewsList, cmp);
        mCardAdapter.notifyDataSetChanged();

    }

    public interface OnRecommendFragmentListener
    {
        MyUser onRecommendFragmentCall();
    }


}
