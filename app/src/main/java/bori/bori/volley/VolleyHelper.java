package bori.bori.volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import bori.bori.news.NewsHelper;
import bori.bori.news.NewsInfo;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bori.bori.news.News;

/**
 * Created by Eugene on 2017-07-18.
 */

public class VolleyHelper
{
    static final private String TAG = "VolleyHelper";

    static final private String LOCAL = "http://125.143.191.213:8000/bori";
    static final public String HEAD_NEWS_URL = LOCAL + "/head";
    static final public String RCMD_NEWS_URL = LOCAL + "/rcmd";

    final int SOCKET_TIME = 60000;

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

    public VolleyHelper(final RequestQueue requestQueue, AppCompatActivity activity,
                        ProgressDialog progressDialog)
    {
        this.mRequestQueue = requestQueue;
        this.mActivity = activity;
        mProgressDialog = progressDialog;

        this.mNewsHelpler = new NewsHelper(mActivity);
    }


    public void setmRecommendNewsListener(OnVolleyHelperRecommendNewsListener mRecommendNewswListener)
    {
        this.mRecommendNewsListener = mRecommendNewswListener;
    }

    public void setmHeadNewsListener(OnVolleyHelperHeadNewsListener mHeadNewsListener)
    {
        this.mHeadNewsListener = mHeadNewsListener;
    }


    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
    {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }


    public JsonObjectRequest jsonRequest(JSONObject jsonObject, String url)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i(TAG,response.toString());

                        NewsInfo newsInfo= parseJSON(response);
                        String newsType = newsInfo.getmNewsType();

                        updateNews(newsInfo, newsType);

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

                        NewsInfo newsInfo= parseJSON(response);
                        String newsType = newsInfo.getmNewsType();

                        updateNews(newsInfo, newsType);

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


    private void updateNews(NewsInfo newsInfo, String newsType)
    {

        Log.i(TAG,"updateNews");
        Log.i(TAG,"NewsType: " + newsType);

        try
        {
            if(News.KEY_HEAD_LINE_NEWS.equals(newsType))
            {
                Log.i(TAG,"update HEAD  NEWS");
                mHeadNewsListener.onNewsUpdate(newsInfo);
            }
            else if( News.KEY_RECOMMEND_NEWS.equals(newsType))
            {
                Log.i(TAG,"update RECM NEWS");

                if(mRecommendNewsListener != null)
                {
                    mRecommendNewsListener.onNewsUpdate(newsInfo);
                }

                //saveRcmdNewsInfo(newsInfo);
            }

        }
        catch (NullPointerException e)
        {
            Log.e(TAG, e.toString());
        }


    }
    private void saveRcmdNewsInfo(NewsInfo newsInfo)
    {
        SharedPreferences prefs = mActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(newsInfo);

        editor.putString(NewsInfo.TAG,json);
        editor.commit();

    }

    private NewsInfo parseJSON(JSONObject response)
    {
        Log.i(TAG,"parseJSON");
        NewsInfo newsInfo = new NewsInfo();

        ArrayList<News> newsArrayList = new ArrayList<News>();
        String newsType ="";
        String category = "";

        try
        {

            newsType = response.getString(News.KEY_NEWS_TYPE);

            JSONArray jsonArray = response.getJSONArray("news_list");

            newsArrayList = mNewsHelpler.getNewsInfo(jsonArray, newsType);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        newsInfo.setNewsType(newsType);
        newsInfo.setNewsList(newsArrayList);

        return newsInfo;

    }

    private String getHttpheader(String url)
    {
        String header = "http:";
        String newUrl = header + url;

        return newUrl;
    }

    /***
    public interface OnVolleyHelperListener
    {
        void onRecommendNewsUpdateListener(NewsInfo newsInfo);
        void onHeadNewsUpdateListener(NewsInfo newsInfo);
    } **/

    public interface OnVolleyHelperHeadNewsListener
    {
        void onNewsUpdate(NewsInfo newsInfo);
    }

    public interface OnVolleyHelperRecommendNewsListener
    {
        void onNewsUpdate(NewsInfo newsInfo);
    }
}
