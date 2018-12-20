package bori.bori.volley;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import bori.bori.news.NewsInfo;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import bori.bori.news.News;
import org.jsoup.select.Elements;

/**
 * Created by Eugene on 2017-07-18.
 */

public class VolleyHelper
{
    static final private String TAG = "VolleyHelper";

    static final private String LOCAL = "http://125.143.191.148:8000/bori";
    static final public String HEAD_NEWS_URL = LOCAL + "/head";
    static final public String RCMD_NEWS_URL = LOCAL + "/rcmd";

    final int SOKET_TIME = 60000;

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

    public VolleyHelper(final RequestQueue requestQueue, AppCompatActivity activity,
                        ProgressDialog progressDialog)
    {
        this.mRequestQueue = requestQueue;
        this.mActivity = activity;
        mProgressDialog = progressDialog;
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

        int socketTimeout = SOKET_TIME;
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

        int socketTimeout = SOKET_TIME;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        return jsonObjectRequest;
    }


    private void updateNews(NewsInfo newsInfo, String newsType)
    {

        Log.i(TAG,"updateNews");

        Log.i(TAG,"NewsType");
        Log.i(TAG,newsType);

        if(null != mHeadNewsListener && News.KEY_HEAD_LINE_NEWS.equals(newsType))
        {
            Log.i(TAG,"update head news!!!!");
            mHeadNewsListener.onNewsUpdate(newsInfo);
        }
        else if(null != mRecommendNewsListener && News.KEY_RECOMMEND_NEWS.equals(newsType))
        {
            Log.i(TAG,"update RECM NEWS");
            mRecommendNewsListener.onNewsUpdate(newsInfo);
        }

    }

    private NewsInfo parseJSON(JSONObject response)
    {
        Log.i(TAG,"parseJSON");
        NewsInfo newsInfo = new NewsInfo();

        ArrayList<News> newsArrayList = new ArrayList<News>();
        String newsType ="";

        try
        {

            newsType = response.getString(News.KEY_NEWS_TYPE);
            JSONArray jsonArray = response.getJSONArray("news_list");


            newsArrayList = getNewsInfo(jsonArray, newsType);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        newsInfo.setNewsType(newsType);
        newsInfo.setNewsList(newsArrayList);

        return newsInfo;

    }

    private ArrayList getNewsInfo(JSONArray jsonArray, String newsType)
    {

        ArrayList<News> newsArrayList = new ArrayList<News>();

        for(int i=0; i < jsonArray.length(); i++)
        {
            News news = new News();
            String id = "";
            String title = "";
            String link = "";
            String imgSrc = "";

            JSONObject jsonObject  = null;
            try
            {
                jsonObject = jsonArray.getJSONObject(i);

                id = jsonObject.getString("id");
                title = jsonObject.getString("title");
                link = jsonObject.getString("link");
                //imgSrc = jsonObject.getString("img src");
                imgSrc = getImgSrc(jsonObject);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
                continue;
            }
            finally
            {
                news.setNewsType(newsType);
                news.setId(id);
                news.setTitle(title);
                news.setUrl(link);
                news.setImgUrl(imgSrc);

                newsArrayList.add(news);
            }

        }

        return newsArrayList;
    }

    private String getImgSrc(JSONObject jsonObject)
    {
        String img_src = "";

        try
        {

            String json = jsonObject.getString("media_content");

            JSONArray jsonArray = new JSONArray(json);

            for(int i=0; i < jsonArray.length(); i++)
            {
                JSONObject jobject = jsonArray.optJSONObject(i);
                img_src = jobject.getString("url");
            }


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if( !img_src.isEmpty())
            return img_src;


        try
        {
            img_src = jsonObject.getString("img_src");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if( !img_src.isEmpty())
            return img_src;

        try
        {

            String  html = "";
            html = jsonObject.getString("summary");

            Document doc = Jsoup.parse(html);
            Element link = doc.selectFirst("img[src]");
            img_src = link.attr("src");
        }
        catch (NullPointerException e)
        {
            return img_src;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return img_src;

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
