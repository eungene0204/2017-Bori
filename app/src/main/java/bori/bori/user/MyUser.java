package bori.bori.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.io.Serializable;

import bori.bori.singleton.AppSingleton;

/**
 * Created by Eugene on 2017-02-27.
 */

public class MyUser
{
    private static final String TAG = "MyUser";
    public static final String KEY_MYUSER = "MyUser";

    private Context mContext;

    private String mName;
    private String mScreenName;
    private String mEmail;
    private String mPofile_url;
    private Bitmap mBitmap;

    public String getScreenName()
    {
        return mScreenName;
    }

    public void setScreenName(String screenName)
    {
        mScreenName = screenName;
    }

    public Bitmap getBitmap()
    {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap)
    {
        mBitmap = bitmap;
    }

    public MyUser(Context mContext)
    {
        this.mContext = mContext;
    }

    public void setName(final String name)
    {
        this.mName = name;
    }

    public void setEmail(final String email)
    {
        this.mEmail = email;
    }

    public void setProfileUrl(final String url)
    {
        this.mPofile_url = url;
    }

    public String getProfileUrl()
    {
        return this.mPofile_url;
    }

    public String getName()
    {
        return mName;
    }

    public String getEmail()
    {
        return mEmail;
    }

}
