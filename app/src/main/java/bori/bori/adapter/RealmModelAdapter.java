package bori.bori.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Eugene on 2017-09-01.
 */

public class RealmModelAdapter<T extends RealmObject> extends RealmBaseAdapter
{
    public RealmModelAdapter(Context context, RealmResults realmResults, boolean automaticUpdate)
    {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return null;
    }
}
