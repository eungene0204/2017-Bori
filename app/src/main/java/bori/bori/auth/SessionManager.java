package bori.bori.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import bori.bori.activity.LoginActivity;
import bori.bori.user.MyUser;

/**
 * Created by Eugene on 2017-02-23.
 */

public class SessionManager
{
    static private final String PREF_NAME = "login_pref";
    static private final String IS_LOGIN = "IsLoggedIn";
    static private final String KEY_NAME = "name";
    static private final String KEY_SCREEN_NAME = "screenName";
    static private final String KEY_EMAIL = "email";
    static private final String KEY_PROFILEPIC = "profilePic";

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    private int PRIVATE_MODE = 0;

    public SessionManager(Context context)
    {
        this.mContext = context;
        this.mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.mEditor = mPref.edit();
    }

    public void createLoginSession(MyUser user)
    {
        mEditor.putBoolean(IS_LOGIN,true);
        mEditor.putString(KEY_NAME,user.getName());
        mEditor.putString(KEY_SCREEN_NAME,user.getScreenName());
        mEditor.putString(KEY_EMAIL, user.getEmail());
        mEditor.putString(KEY_PROFILEPIC, user.getProfileUrl());

        mEditor.commit();
    }

    public void checkLogin()
    {
        if(!isLoggedIn())
        {
            startLoginActivity();
        }
    }

    private void startLoginActivity()
    {
        Intent i = new Intent(mContext, LoginActivity.class);
        //close all activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //start new task
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(i);

    }

    public void logoutUser()
    {
        mEditor.clear();
        mEditor.commit();

        Intent i = new Intent(mContext,LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(i);
    }


    public String getUserName()
    {
        String name = mPref.getString(KEY_NAME,null);
        return name;
    }

    public String getUserEmail()
    {
        String email = mPref.getString(KEY_EMAIL,null);
        return email;
    }

    public String getProfilePicUrl()
    {
        String url = mPref.getString(KEY_PROFILEPIC,null);
        return url;
    }

    public String getScreenName()
    {
        String screenName = mPref.getString(KEY_SCREEN_NAME,null);
        return screenName;
    }

    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_NAME, mPref.getString(KEY_NAME,null));
        user.put(KEY_EMAIL, mPref.getString(KEY_EMAIL,null));

        return user;
   }

    private boolean isLoggedIn()
    {
        return mPref.getBoolean(IS_LOGIN,false);
    }
}
