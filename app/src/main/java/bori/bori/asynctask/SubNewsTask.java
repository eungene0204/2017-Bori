package bori.bori.asynctask;

import android.app.Activity;
import androidx.recyclerview.widget.LinearLayoutManager;
import bori.bori.adapter.RecommendCardAdapter;
import bori.bori.adapter.RecommendSubNewsAdapter;
import bori.bori.news.News;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

import java.util.List;

public class SubNewsTask extends AsyncTask<RecommendCardAdapter.ViewHolder, Void, Void>
{
    private Activity mActivity;
    private List<News> mNewsList;

    public SubNewsTask(Activity activity,List<News> list)
    {
        this.mActivity = activity;
        this.mNewsList = list;
    }


    @Override
    protected Void doInBackground(RecommendCardAdapter.ViewHolder... viewHolders)
    {
        RecommendCardAdapter.ViewHolder viewHolder = viewHolders[0];

        List<News> subList;
        if(mNewsList.size() > 5)
            subList = mNewsList.subList(0,5);
        else
            subList = mNewsList;

        RecommendSubNewsAdapter adapter = new RecommendSubNewsAdapter(subList,mActivity);

        LinearLayoutManager layoutManager = new
                LinearLayoutManager(mActivity.getApplication());

        viewHolder.mBinding.subNewsList.setLayoutManager(layoutManager);
        viewHolder.mBinding.subNewsList.setNestedScrollingEnabled(false);
        viewHolder.mBinding.subNewsList.setAdapter(adapter);

        return null;
    }
}
