package bori.bori.news;

/**
 * Created by Eugene on 2017-08-10.
 */

public class News
{
    public static String KEY_ID ="id";
    public static String KEY_URL = "url";
    public static String KEY_TITLE = "title";
    public static String KEY_IMG_URL = "imgUrl";
    public static String KEY_FONT_SIZE = "fontSize";

    private String mTitle;
    private String mLink;
    private String mImgSrc;
    private String mId;

    public String getId()
    {
        return mId;
    }

    public void setId(String id)
    {
        mId = id;
    }


    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public String getUrl()
    {
        return mLink;
    }

    public void setUrl(String link)
    {
        mLink = link;
    }

    public String getImgUrl()
    {
        return mImgSrc;
    }

    public void setImgUrl(String imgSrc)
    {
        mImgSrc = imgSrc;
    }
}
