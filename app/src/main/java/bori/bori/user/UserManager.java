package bori.bori.user;

import android.content.Context;

import com.twitter.sdk.android.core.models.User;

import bori.bori.auth.SessionManager;

public class UserManager
{
    static private SessionManager session;

    private UserManager()
    {
    }

    static public void sessionCheck(Context context)
    {
        session = new SessionManager(context);
        session.checkLogin();
    }

    static public void setUser(MyUser user, Context context)
    {
        user.setName(session.getUserName());
        user.setEmail(session.getUserEmail());
        user.setScreenName(session.getScreenName());
        user.setProfileUrl(session.getProfilePicUrl());
    }




}
