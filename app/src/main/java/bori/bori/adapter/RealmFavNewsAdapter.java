package bori.bori.adapter;

import android.content.Context;

import bori.bori.realm.FavNews;
import io.realm.RealmResults;

/**
 * Created by Eugene on 2017-09-01.
 */
public class RealmFavNewsAdapter extends RealmModelAdapter<FavNews>
{
    public RealmFavNewsAdapter(Context context, RealmResults realmResults, boolean automaticUpdate)
    {
        super(context, realmResults, automaticUpdate);
    }
}
