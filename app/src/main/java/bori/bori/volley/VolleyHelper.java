package bori.bori.volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bori.bori.R;
import bori.bori.fragment.RecommendFragment;
import bori.bori.news.News;
import bori.bori.user.MyUser;

/**
 * Created by Eugene on 2017-07-18.
 */

public class VolleyHelper
{

    static final private String TAG = "VolleyHelper";
    //static final private String url = "http://127.0.0.1:8000/bori/";
    static final private String url = "http://125.143.191.179:8000/bori/";

    static final private String USER_TAG = "UserTAG";

    private AppCompatActivity mActivity;
    private RequestQueue mRequestQueue;

    private ProgressDialog mProgressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private OnVolleyHelperListener mOnVolleyHelperListener;

    public VolleyHelper(final RequestQueue requestQueue, AppCompatActivity activity,
                        ProgressDialog progressDialog)
    {
        this.mRequestQueue = requestQueue;
        this.mActivity = activity;
        mProgressDialog = progressDialog;
    }

    public void setOnVolleyHelperListener(OnVolleyHelperListener listener)
    {
        mOnVolleyHelperListener = listener;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
    {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }

    public JSONObject writeJSON(MyUser user)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("screenName", user.getScreenName());
            jsonObject.put("name", user.getName());
            jsonObject.put("email", user.getEmail());

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jsonObject;

    }

    public JsonObjectRequest jsonRequest(JSONObject jsonObject)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i(TAG,response.toString());
                        ArrayList<News> newsArrayList = parseJSON(response);

                        if(null != mOnVolleyHelperListener)
                            mOnVolleyHelperListener.onNewsListener(newsArrayList);

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

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        return jsonObjectRequest;
    }

    private ArrayList<News> parseJSON(JSONObject response)
    {
        Log.i(TAG,"parseJSON");
        ArrayList<News> newsArrayList = new ArrayList<News>();

        try
        {
            JSONArray jsonArray = response.getJSONArray("news_list");
            for(int i=0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject  = jsonArray.getJSONObject(i);
                News news = new News();

                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                String link = jsonObject.getString("link");
                String imgSrc = jsonObject.getString("img_src");
                imgSrc = getHttpheader(imgSrc);

                news.setId(id);
                news.setTitle(title);
                news.setUrl(link);
                news.setImgUrl(imgSrc);

                newsArrayList.add(news);

            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return newsArrayList;

    }

    private String getHttpheader(String url)
    {
       String header = "http:";
       String newUrl = header + url;

       return newUrl;
    }

    public interface OnVolleyHelperListener
    {
        public void onNewsListener(ArrayList<News> newsArrayList);
    }
}
