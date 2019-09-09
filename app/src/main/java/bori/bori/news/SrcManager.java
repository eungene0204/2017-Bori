package bori.bori.news;

import org.json.JSONException;
import org.json.JSONObject;

public class SrcManager
{
    private SrcManager()
    {
    }

    static public String getSource(String source) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(source);

        String src = jsonObject.getString("title");

        return src;

    }

    static public String getSourceUrl(String source) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(source);

        String url = jsonObject.getString("href");

        return url;
    }

}
