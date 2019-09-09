package bori.bori.application;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import bori.bori.R;
import bori.bori.news.SrcLogoManager;

public class BoriApplication extends Application
{
    public static final String NIGHT_MODE= "NIGHT_MODE";

    private boolean mIsNightModeEnabled = false;

    private static BoriApplication mInstance = null;

    public static BoriApplication getInstance()
    {

        if(mInstance == null)
        {
            mInstance = new BoriApplication();
        }

        return mInstance;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mIsNightModeEnabled = preferences.getBoolean(NIGHT_MODE, false);

    }

    public boolean isNightModeEnabled()
    {
        return mIsNightModeEnabled;
    }

    public void setNightModeEnabled(boolean nightModeEnabled)
    {
        mIsNightModeEnabled = nightModeEnabled;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(NIGHT_MODE, mIsNightModeEnabled);
        editor.apply();
    }

    public void checkDarkTheme(Activity activity)
    {
        if(isNightModeEnabled())
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setDarkTheme(activity);

        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

    }

    private void setDarkTheme(Activity activity)
    {
        activity.setTheme(R.style.DarkAppTheme);
    }

}
