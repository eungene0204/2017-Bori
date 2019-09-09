package bori.bori.volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import bori.bori.R;
import bori.bori.databinding.RcmdNewsSubItemBinding;
import bori.bori.fragment.EmptyFragment;
import bori.bori.fragment.FragmentHelper;
import bori.bori.news.Category;
import bori.bori.news.NewsHelper;
import bori.bori.news.NewsInfo;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bori.bori.news.News;

/**
 * Created by Eugene on 2017-07-18.
 */

public class VolleyHelper
{
    static final private String TAG = "VolleyHelper";

    static final private String LOCAL = "http://192.168.0.4:8000/bori";
    static final public String HEAD_NEWS_URL = LOCAL + "/head";
    static final public String RCMD_NEWS_URL = LOCAL + "/rcmd";

    final int SOCKET_TIME = 50000;

    /*
     static final private String url = "http://ec2-13-125-11-197.ap-northeast-2.compute.amazonaws.com" +
            ":8000/bori/"; */


    static final private String USER_TAG = "UserTAG";

    private AppCompatActivity mActivity;
    private RequestQueue mRequestQueue;

    private ProgressDialog mProgressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //private OnVolleyHelperListener mOnVolleyHelperListener;

    private OnVolleyHelperHeadNewsListener mHeadNewsListener;
    private OnVolleyHelperRecommendNewsListener mRecommendNewsListener;

    private NewsHelper mNewsHelpler;
    private List<RcmdNewsSubItemBinding> mRcmdNewsSubItemBindings;

    public VolleyHelper(final RequestQueue requestQueue, AppCompatActivity activity,
                        ProgressDialog progressDialog)
    {
        this.mRequestQueue = requestQueue;
        this.mActivity = activity;
        mProgressDialog = progressDialog;

        this.mNewsHelpler = new NewsHelper(mActivity);
        this.mRcmdNewsSubItemBindings = new ArrayList<>();
    }


    public void setRecommendNewsListener(OnVolleyHelperRecommendNewsListener mRecommendNewswListener)
    {
        this.mRecommendNewsListener = mRecommendNewswListener;
    }

    public void setHeadNewsListener(OnVolleyHelperHeadNewsListener mHeadNewsListener)
    {
        this.mHeadNewsListener = mHeadNewsListener;
    }


    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
    {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }


    public JsonObjectRequest headRequest(JSONObject jsonObject, String url)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i(TAG,response.toString());

                        NewsInfo newsInfo= mNewsHelpler.getNewsInfo(response);
                        String newsType = newsInfo.getNewsType();

                        mNewsHelpler.updateNews(newsInfo, newsType, mHeadNewsListener);

                        if(null!=mProgressDialog)
                            mProgressDialog.dismiss();

                        if(null != mSwipeRefreshLayout)
                            mSwipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG,error.toString());

                        if(null!=mProgressDialog)
                            mProgressDialog.dismiss();

                        if(null != mSwipeRefreshLayout)
                            mSwipeRefreshLayout.setRefreshing(false);
                    }
                });

        int socketTimeout = SOCKET_TIME;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        return jsonObjectRequest;
    }

    public JsonObjectRequest rcmdRequest(JSONObject jsonObject, String url)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i(TAG,response.toString());

                        NewsInfo newsInfo= mNewsHelpler.getNewsInfo(response);
                        if(newsInfo == null)
                            return;

                        String newsType = newsInfo.getNewsType();

                        mNewsHelpler.updateNews(newsInfo, newsType, mRecommendNewsListener);

                        if(null!=mProgressDialog)
                            mProgressDialog.dismiss();

                        if(null != mSwipeRefreshLayout)
                            mSwipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG,error.toString());
                if(error instanceof NoConnectionError ||error instanceof ServerError)
                {
                    String msg = mActivity.getResources().
                            getString(R.string.connection_error);
                    FragmentHelper.setEmptyFragment(mActivity, msg);
                }


                if(null != mSwipeRefreshLayout)
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        int socketTimeout = SOCKET_TIME;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        return jsonObjectRequest;
    }

    public interface OnNewsUpdateListener
    {
        void onNewsUpdate(NewsInfo newsInfo);
    }

    public interface OnVolleyHelperHeadNewsListener extends OnNewsUpdateListener
    {
    }

    public interface OnVolleyHelperRecommendNewsListener extends OnNewsUpdateListener
    {
    }
}
