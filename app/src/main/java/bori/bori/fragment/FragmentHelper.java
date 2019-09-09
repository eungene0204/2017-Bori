package bori.bori.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import bori.bori.R;
import bori.bori.utility.FontUtils;

public class FragmentHelper
{
    static public boolean mIsServerErr;

    static public void checkFragment(Activity activity, Fragment fragment, String tag, String msg)
    {
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        Fragment currentFragment = fragment;


        Fragment nextFragment = appCompatActivity.getSupportFragmentManager()
                .findFragmentByTag(tag);

        if(nextFragment != null)
        {
            if(!nextFragment.isVisible())
            {
                currentFragment = nextFragment;
                if(currentFragment instanceof EmptyFragment)
                {
                    EmptyFragment emptyFragment = new EmptyFragment(msg);
                    currentFragment = emptyFragment;
                }

            }
        }

        replaceFragment(activity, currentFragment, tag);
    }

    static public void replaceFragment(Activity activity, Fragment fragment, String tag)
    {
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;

        FragmentTransaction ft = appCompatActivity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment, tag);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
        //ft.commit();

    }

    static public void setEmptyFragment(Activity activity, String msg)
    {
        Fragment fragment = new EmptyFragment(msg);
        checkFragment(activity, fragment, EmptyFragment.TAG, msg);
    }

}
