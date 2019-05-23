package bori.bori.news;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eugene on 2017-08-10.
 */

public class News implements Parcelable
{
    public static String TAG = "News";

    public static String KEY_URL_TYPE = "url_type";
    public static String KEY_NEWS_TYPE = "news_type";
    public static String KEY_HEAD_LINE_NEWS = "head_line_news";
    public static String KEY_RECOMMEND_NEWS = "recommend_news";
    public static String KEY_ID ="id";
    public static String KEY_URL = "url";
    public static String KEY_TITLE = "title";
    public static String KEY_IMG_URL = "imgUrl";
    public static String KEY_FONT_SIZE = "fontSize";
    public static String KEY_CATEGORY = "category";

    private String mNewsType;
    private String mTitle;
    private String mLink;
    private String mImgSrc;
    private String mId;
    private String mSource;
    private String mSourceUrl;
    private String mDate;
    private String mCategory;
    private String mOriginal;


    private String mSimilarityLevel;
    private Drawable mSourceLogo;

    public News()
    {

    }

    protected News(Parcel in)
    {
        mNewsType = in.readString();
        mTitle = in.readString();
        mLink = in.readString();
        mImgSrc = in.readString();
        mId = in.readString();
        mSource = in.readString();
        mSourceUrl = in.readString();
        mDate = in.readString();
        mCategory = in.readString();
        mSimilarityLevel = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>()
    {
        @Override
        public News createFromParcel(Parcel in)
        {
            return new News(in);
        }

        @Override
        public News[] newArray(int size)
        {
            return new News[size];
        }
    };

    public String getSimilarityLevel()
    {
        return mSimilarityLevel;
    }

    public void setSimilarityLevel(String similarityLevel)
    {
        mSimilarityLevel = similarityLevel;
    }


    public void setSourceLogo(Drawable logo)
    {
        mSourceLogo = logo;
    }

    public Drawable getSourceLogo()
    {
        return mSourceLogo;
    }

    public String getSource()
    {
        return mSource;
    }

    public void setSource(String source)
    {
        mSource = source;
    }

    public String getDate()
    {
        return mDate;
    }

    public void setDate(String date)
    {
        mDate = date;
    }

    public String getCategory()
    {
        return mCategory;
    }

    public void setCategory(String category)
    {
        mCategory = category;
    }


    public String getOriginal()
    {
        return mOriginal;
    }
    public void setOriginal(String original)
    {
        mOriginal = original;
    }

    public String getSourceUrl()
    {
        return mSourceUrl;
    }

    public void setSourceUrl(String sourceUrl)
    {
        mSourceUrl = sourceUrl;
    }

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

    public String getNewsType()
    {
        return mNewsType;
    }

    public void setNewsType(String mNewsType)
    {
        this.mNewsType = mNewsType;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mNewsType);
        dest.writeString(mTitle);
        dest.writeString(mLink);
        dest.writeString(mImgSrc);
        dest.writeString(mId);
        dest.writeString(mSource);
        dest.writeString(mSourceUrl);
        dest.writeString(mDate);
        dest.writeString(mCategory);
        dest.writeString(mSimilarityLevel);
    }
}
