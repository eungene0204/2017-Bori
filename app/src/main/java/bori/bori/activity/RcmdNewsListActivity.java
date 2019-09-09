package bori.bori.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bori.bori.R;
import bori.bori.adapter.RecommendListAdapter;
import bori.bori.news.News;
import bori.bori.news.NewsHelper;
import bori.bori.news.SrcLogoManager;

import java.util.List;

public class RcmdNewsListActivity extends AppCompatActivity
{
    private List<News> mNewsList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsHelper mNewsHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remd_news_list);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(),R.color.GreyPrimary));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.news_list));


        getNewsList();

        setRecyclerView();

    }

    private void setRecyclerView()
    {
        mRecyclerView = findViewById(R.id.news_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecommendListAdapter(this,mNewsList,
                getSupportFragmentManager(),mRecyclerView );

        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new
                DividerItemDecoration(this, LinearLayoutManager.VERTICAL);

        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,
                R.drawable.divider));

        mRecyclerView.addItemDecoration(dividerItemDecoration);


    }

    private void getNewsList()
    {
        mNewsHelper = new NewsHelper(this);
        mNewsList = getIntent().getParcelableArrayListExtra(News.KEY_NEWS_LIST);

        for(News news : mNewsList)
            news.setSourceLogo(SrcLogoManager.getInstance().findSourceLogo(news.getSource()));
            //news.setSourceLogo(mNewsHelper.getSourceLogo(news.getSource()));
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
