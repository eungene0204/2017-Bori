package bori.bori.realm;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;
import bori.bori.R;
import bori.bori.news.News;
import bori.bori.utility.TimeUtils;
import io.realm.*;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

import java.util.Calendar;
import java.util.List;

public class RealmHelper
{
    public final static String TAG = "RealmHelper";

    public final static String TYPE_TODAY = "today";
    public final static String TYPE_SAVE = "save";
    public final static String TYPE_WEEK_TODAY = "week_today";
    public final static String TYPE_WEEK_SAVED = "week_saved";

    private Realm mRealm;
    private Context mContext;
    private SavedCategory mCategories;

    public RealmHelper(Context context)
    {
        mRealm = Realm.getDefaultInstance();
        this.mContext = context;
    }

    public Realm getRealm()
    {
        return mRealm;
    }


    public void bookMarkNews(final News news)
    {
        mRealm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                try
                {
                    FavNews favNews = realm.createObject(FavNews.class, news.getId());

                    favNews.setId(news.getId());
                    favNews.setTitle(news.getTitle());
                    favNews.setUrl(news.getUrl());
                    favNews.setDate(news.getDate());
                    favNews.setImgUrl(news.getImgUrl());
                    favNews.setSource(news.getSource());
                    favNews.setCategory(news.getCategory());

                    realm.insertOrUpdate(favNews);

                    addSavedCategory(favNews.getCategory(), realm);

                    String msg = mContext.getResources().
                            getString(R.string.toast_bookmark_success);

                    Toast.makeText(mContext,msg, Toast.LENGTH_SHORT).show();
                }
                catch(RealmPrimaryKeyConstraintException e)
                {
                   Vibrator vibrator =
                           (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

                    if(vibrator.hasVibrator())
                    {
                        vibrator.vibrate(50);
                    }

                    String msg = mContext.getResources().getString(R.string.toast_bookmark_already);

                    Toast.makeText(mContext,msg, Toast.LENGTH_SHORT)
                            .show();

                }

            }
        });
    }

    private void addSavedCategory(String category, Realm realm)
    {
        SavedCategory result = mRealm.where(SavedCategory.class)
                .equalTo("mCategory", category).findFirst();


        if(null != result)
        {
            result.increaseVal();
        }
        else
        {
            SavedCategory newCategory = new SavedCategory();
            newCategory.setCategory(category);
            newCategory.setValue(1);

            int today = TimeUtils.getToday();

            newCategory.setToday(today);

            realm.insertOrUpdate(newCategory);
        }

    }

    public void addTodayCategory(String category)
    {
        mRealm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {

                TodayCategory result = realm.where(TodayCategory.class)
                        .equalTo("mCategory", category).findFirst();

                if(null != result)
                {
                    if(result.getToday() != TimeUtils.getToday())
                    {
                        result.setToday(TimeUtils.getToday());
                        result.setValue(1);
                    }
                    else
                    {
                        result.increaseVal();
                    }

                }
                else
                {
                    TodayCategory newCategory = new TodayCategory();
                    newCategory.setCategory(category);
                    newCategory.setValue(1);

                    int today = TimeUtils.getToday();

                    newCategory.setToday(today);

                    realm.insertOrUpdate(newCategory);
                }

            }
        });

    }

    public RealmResults<FavNews> getAllSavedNews()
    {

        RealmResults<FavNews> results;

        results = mRealm.where(FavNews.class)
                .findAll();

        return results;
    }

    public RealmResults getCategory(String category)
    {
        if(category.equals(TYPE_TODAY))
        {
            RealmResults<TodayCategory> results;

            results = mRealm.where(TodayCategory.class)
                    .in("mToday",TimeUtils.getTodayArray())
                    .sort("mValue", Sort.DESCENDING).limit(5).
                            findAll();

            return results;
        }
        else if(category.equals(TYPE_SAVE))
        {
            RealmResults<SavedCategory> results;

            results = mRealm.where(SavedCategory.class)
                    .in("mToday",TimeUtils.getTodayArray())
                    .sort("mValue", Sort.DESCENDING).limit(5).
                            findAll();

            return results;

        }
        else if(category.equals(TYPE_WEEK_TODAY))
        {
            RealmResults<TodayCategory> temp;
            RealmResults<TodayCategory> results;

            temp = mRealm.where(TodayCategory.class)
                    .findAll();

            results = mRealm.where(TodayCategory.class)
                    .greaterThanOrEqualTo("mToday", TimeUtils.getSevenDaysAgo())
                    .lessThanOrEqualTo("mToday", TimeUtils.getToday())
                    .sort("mValue",Sort.DESCENDING).limit(5)
                    .findAll();

            return results;

        }
        else if(category.equals((TYPE_WEEK_SAVED)))
        {
            RealmResults<SavedCategory> results;

            results = mRealm.where(SavedCategory.class)
                    .greaterThanOrEqualTo("mToday", TimeUtils.getSevenDaysAgo())
                    .lessThanOrEqualTo("mToday", TimeUtils.getToday())
                    .sort("mValue", Sort.DESCENDING).limit(5)
                    .findAll();

            return results;
        }

        return null;

    }

    public void delete(final String id, String category)
    {
        mRealm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                try
                {
                     realm.where(FavNews.class).equalTo("id", id)
                        .findFirst()
                        .deleteFromRealm();

                     decreaseCategory(category);

                }
                catch (NullPointerException e)
                {
                    Log.e(TAG, String.valueOf(e));
                    return;
                }

                String msg = "뉴스가 삭제 되었습니다.";
                Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void decreaseCategory(String category)
    {
        SavedCategory _category = mRealm.where(SavedCategory.class).
                equalTo("mCategory", category)
                .findFirst();

        if(_category == null)
        {
            return;
        }
        else
        {
            _category.decreaseVal();
            if(_category.getValue() == 0)
            {
                mRealm.where(SavedCategory.class).equalTo("mCategory", category).findFirst()
                        .deleteFromRealm();
            }
        }

    }

    public void deleteAll()
    {
        mRealm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                mRealm.deleteAll();

            }
        });
    }
}
