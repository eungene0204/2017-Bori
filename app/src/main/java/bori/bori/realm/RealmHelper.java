package bori.bori.realm;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;
import bori.bori.R;
import bori.bori.news.FavNews;
import bori.bori.news.News;
import io.realm.*;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class RealmHelper
{
    public final static String TAG = "RealmHelper";

    private Realm mRealm;
    private Context mContext;
    private Category mCategories;

    public RealmHelper(Context context)
    {
        mRealm = Realm.getDefaultInstance();
        this.mContext = context;
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

                    addCategory(favNews.getCategory(), realm, favNews.getId());

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

    private void addCategory(String category, Realm realm, String id)
    {
        Category result = mRealm.where(Category.class)
                .equalTo("mCategory", category).findFirst();


        if(null != result)
        {
            result.increaseVal();
        }
        else
        {
            Category newCategory = new Category();
            newCategory.setCategory(category);
            newCategory.setValue(1);

            realm.insertOrUpdate(newCategory);
        }

    }

    public RealmResults<FavNews> getAllSavedNews()
    {

        RealmResults<FavNews> results;

        results = mRealm.where(FavNews.class)
                .findAll();

        return results;
    }

    public RealmResults<Category> getCategory()
    {
        RealmResults<Category> results;

        results = mRealm.where(Category.class)
                .sort("mValue", Sort.DESCENDING).limit(5).
                        findAll();

        return results;
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
        Category _category = mRealm.where(Category.class).
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
                mRealm.where(Category.class).equalTo("mCategory", category).findFirst()
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
