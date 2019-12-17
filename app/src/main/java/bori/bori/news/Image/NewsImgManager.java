package bori.bori.news.Image;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import bori.bori.R;

public class NewsImgManager
{
    private NewsImgManager()
    {
    }

    static public String getImgSrc(JSONObject jsonObject)
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

    static public void setRoundedImg(ImageView imgview, Context context)
    {
        GradientDrawable drawable = (GradientDrawable)context.getDrawable(R.drawable.round_background);
        imgview.setBackground(drawable);
        imgview.setClipToOutline(true);

    }


}
