package bori.bori.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;

/**
 * Created by Eugene on 2017-09-01.
 */

public class RealmRecyclerViewAdapter<T extends RealmObject> extends RecyclerView.Adapter
{
    private RealmBaseAdapter<T> mRealmBaseAdapter;

    public T getItem(int position)
    {
        return mRealmBaseAdapter.getItem(position);

    }

    public RealmBaseAdapter<T> getRealmAdapter()
    {
        return mRealmBaseAdapter;
    }

    public void setRealmBaseAdapter(RealmBaseAdapter<T> realmBaseAdapter)
    {
        mRealmBaseAdapter = realmBaseAdapter;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }


}
