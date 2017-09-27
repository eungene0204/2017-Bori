package bori.bori.realm;

import android.app.Activity;
import android.app.Application;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Eugene on 2017-09-01.
 */

public class RealmController
{
    private static RealmController mInstance;
    private final Realm mRealm;

    public RealmController(Application application)
    {
        mRealm = Realm.getDefaultInstance();
    }

    public static RealmController with(Activity activity)
    {
        if(mInstance == null)
        {
            mInstance = new RealmController(activity.getApplication());
        }

        return mInstance;
    }

    public static RealmController getInstance()
    {
        return mInstance;
    }

    public Realm getRealm()
    {
        return mRealm;
    }

    public void refresh()
    {
        mRealm.refresh();
    }

    public void clearAll()
    {
        mRealm.beginTransaction();
        mRealm.clear(FavNews.class);
        mRealm.commitTransaction();
    }

    public RealmResults<FavNews> getFavNewsAll()
    {
        return mRealm.where(FavNews.class).findAll();
    }

    public FavNews getFavNews(String id)
    {
        return mRealm.where(FavNews.class).equalTo("id",id)
                .findFirst();
    }

    public int getIndexOf(FavNews favNews)
    {
        RealmResults<FavNews> allNews = getFavNewsAll();

          for(int i=0; i < allNews.size(); i++)
        {
            if(allNews.get(i).equals(favNews))
            {
                return i;
            }
        }

        return 0;

    }

    public boolean hasFavNews()
    {
        return !mRealm.allObjects(FavNews.class).isEmpty();
    }


}
