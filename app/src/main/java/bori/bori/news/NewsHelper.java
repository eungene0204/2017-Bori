package bori.bori.news;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import bori.bori.R;
import bori.bori.fragment.EmptyFragment;
import bori.bori.fragment.FragmentHelper;
import bori.bori.news.Image.NewsImgManager;
import bori.bori.news.date.NewsDateManager;
import bori.bori.news.source.SrcLogoManager;
import bori.bori.news.source.SrcManager;
import bori.bori.utility.JsonUtils;
import bori.bori.utility.TimeUtils;
//import org.json.JSONArray;
import bori.bori.volley.VolleyHelper;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsHelper
{
    public static final String TAG = "NewsHelper";

    private Activity mActivity;

    public NewsHelper(Activity activity)
    {
        this.mActivity = activity;
    }

    private ArrayList getNewsList(JSONArray jsonArray, String newsType )
    {
        ArrayList<News> newsArrayList = new ArrayList<News>();

        for(int i=0; i < jsonArray.length(); i++)
        {
            News news = new News();

            String id = "";
            String title = "";
            String link = "";
            String imgSrc = "";
            String source = "";
            String sourceUrl = "";
            String date = "";
            String category = "";
            String original = "";
            String simLelvel = "";
            String today = "";

            JSONObject jsonObject  = null;
            try
            {
                jsonObject = jsonArray.getJSONObject(i);

                id = jsonObject.getString("id");
                source = SrcManager.getSource(jsonObject.getString("source"));
                title = NewsTitleManager.getTitle(jsonObject.getString("title"), source);
                //title = jsonObject.getString("title");

                if(newsType.equals(News.KEY_RECOMMEND_NEWS))
                {
                    original = jsonObject.getString("original");
                }

                link = jsonObject.getString("link");
                //imgSrc = jsonObject.getString("img src");
                imgSrc = NewsImgManager.getImgSrc(jsonObject);
                sourceUrl = SrcManager.getSourceUrl(jsonObject.getString("source"));
                //date = NewsDateManager.getDate(jsonObject.getString("published"));
                date = "";
                category = jsonObject.getString(News.KEY_CATEGORY);
                simLelvel = jsonObject.getString("sim_level");

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
                news.setDate(date);
                news.setSource(source);
                news.setSourceUrl(sourceUrl);
                news.setCategory(category);
                news.setSourceLogo(SrcLogoManager.getInstance().findSourceLogo(source));
                news.setSimilarityLevel(simLelvel);
                news.setOriginal(original);
                news.setToday(String.valueOf(TimeUtils.getToday()));

                newsArrayList.add(news);
            }

        }

        return newsArrayList;
    }


    @Nullable
    public NewsInfo getNewsInfo(JSONObject response)
    {
        NewsInfo newsInfo = new NewsInfo();
        String newsType = "";
        List<News> newsList = new ArrayList<>();

        try
        {
            newsType = JsonUtils.parseNewsType(response);
            JSONArray jsonArray = JsonUtils.getNewsJSONArray(response);

            if(checkEmptyNews(jsonArray))
            {
                showEmtpyNewsFragment();
                return null;
            }

            
            newsList = getNewsList(jsonArray,newsType);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        newsInfo.setNewsType(newsType);
        newsInfo.setNewsList(newsList);

        return newsInfo;

    }

    private void showEmtpyNewsFragment()
    {
        String msg = mActivity.getResources().getString(R.string.no_news);
        Fragment fragment = new EmptyFragment(msg);
        FragmentHelper.checkFragment(mActivity,fragment, EmptyFragment.TAG, msg);

    }

    private boolean checkEmptyNews(JSONArray jsonArray)
    {
        if(jsonArray.length() <= 0)
            return true;
        else
            return false;
    }

    public void updateNews(NewsInfo newsInfo, String newsType,
                            VolleyHelper.OnNewsUpdateListener listener)
    {
        Log.i(TAG,"updateNews");
        Log.i(TAG,"NewsType: " + newsType);

        try
        {
            if(News.KEY_HEAD_LINE_NEWS.equals(newsType))
            {
                Log.i(TAG,"update HEAD  NEWS");
                listener.onNewsUpdate(newsInfo);
            }
            else if( News.KEY_RECOMMEND_NEWS.equals(newsType))
            {
                Log.i(TAG,"update RECM NEWS");

                if(listener != null)
                {
                    listener.onNewsUpdate(newsInfo);
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


    public interface OnSortListener
    {
        void onSort(String sortType);
    }

}
