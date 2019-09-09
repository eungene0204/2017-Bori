package bori.bori.utility;

import android.util.Log;
import bori.bori.news.News;
import bori.bori.news.NewsInfo;
import bori.bori.user.MyUser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils
{
    static public final String TAG = "JsonUtils";

    private JsonUtils()
    {
    }

    static public JSONObject writeJSON(MyUser user)
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

    static public JSONArray getNewsJSONArray(JSONObject response) throws JSONException
    {
        JSONArray jsonArray = response.getJSONArray("news_list");

        return jsonArray;
    }

    static public String parseNewsType(JSONObject response) throws JSONException
    {
        String newsType = response.getString(News.KEY_NEWS_TYPE);

        return newsType;

    }


}
