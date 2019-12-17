package bori.bori.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bori.bori.R;
import bori.bori.activity.RcmdNewsListActivity;
import bori.bori.activity.WebViewActivity;
import bori.bori.databinding.RcmdNewsCardHeaderBinding;
import bori.bori.databinding.RcmdNewsSubItemBinding;
import bori.bori.fragment.bottom.RcmdNewsBottomSheetDialogFragment;
import bori.bori.news.Category;
import bori.bori.news.News;
import bori.bori.news.NewsListManager;
import bori.bori.news.source.SrcLogoManager;

import java.util.ArrayList;
import java.util.List;

public class RecommendCardAdapter extends RecyclerView.Adapter
{
    static final String TAG = "RemdCardAdapter";

    private FragmentManager mFragmentManager;
    private Context mContext;
    private List<Category> mCategoryList;
    private FragmentActivity mActivity;
    private LayoutInflater mLayoutInflater;
    private NewsListManager mNewsListManager;

    public RecommendCardAdapter(FragmentManager fragmentManager,List<Category> categoryList,
                                Context context, FragmentActivity activity)
    {
        mCategoryList = categoryList;
        mFragmentManager = fragmentManager;
        mContext = context;
        mActivity = activity;

        mLayoutInflater= (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        /*
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcmd_news_card_header,
                parent,false); */

        RcmdNewsCardHeaderBinding binding = RcmdNewsCardHeaderBinding.
                inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new HeaderViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
        Category category = mCategoryList.get(position);
        SrcLogoManager srcLogoManager = SrcLogoManager.getInstance();

        viewHolder.bind(category);

        setLevelBar(category.getLevel(),viewHolder);

        addSubNews(category.getNewsList(), viewHolder);

        /*
        setSubNewsList(viewHolder,category.getNewsList());
        setSubNews(viewHolder, category.getNewsList());
        */

        setLastItemLayoutMargin(viewHolder, position);

    }

    private void addSubNews(List<News> newsList, HeaderViewHolder viewHolder)
    {
        mNewsListManager = new NewsListManager(newsList);
        List<News> subList = mNewsListManager.getSubNewsList(5);
        LinearLayout linearLayout = viewHolder.mBinding.linearLayout;

        for(News news : subList)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);


            RcmdNewsSubItemBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.rcmd_news_sub_item, null, false);

            binding.setNews(news);
            View root = binding.getRoot();
            linearLayout.addView(root);

        }

    }


    private void setLastItemLayoutMargin(HeaderViewHolder viewHolder, int position)
    {
        if(position == getItemCount() - 1)
        {
            CardView view =
                    viewHolder.mBinding.getRoot().findViewById(R.id.card_view);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                    view.getLayoutParams();

            params.setMargins(0,0,0,0);
            view.setLayoutParams(params);
        }

    }



    private void addShowMoreView(List<News> newsList, LinearLayout linearLayout)
    {
        if(5 < newsList.size())
        {
            View view = mLayoutInflater.inflate(R.layout.rcmd_show_more,null);
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   startNewsActivity(newsList);
                    
                }
            });

            linearLayout.addView(view);

        }
    }

    private void startNewsActivity(List<News> newsList)
    {
        Intent intent = new Intent(mActivity, RcmdNewsListActivity.class);

        intent.putParcelableArrayListExtra(News.KEY_NEWS_LIST,(ArrayList<News>)newsList);
        mContext.startActivity(intent);

    }




    @Override
    public int getItemCount()
    {
        return mCategoryList == null ? 0 : mCategoryList.size();
    }

    private void startWebViewActivity(News news)
    {
        Intent intent = new Intent(mContext, WebViewActivity.class);

        intent.putExtra(News.TAG, news);
        intent.putExtra(News.KEY_URL_TYPE, WebViewActivity.TYPE_NEWS_URL);
        //intent.putExtra(News.KEY_FONT_SIZE, getFontSize());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(intent);

    }

    @Override
    public long getItemId(int position)
    {
        return mCategoryList.get(position).hashCode();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder
    {

        public RcmdNewsCardHeaderBinding mBinding;

        public HeaderViewHolder(RcmdNewsCardHeaderBinding binding)
        {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        public void bind(Category category)
        {
            mBinding.setCategory(category);
            mBinding.executePendingBindings();


            mBinding.cardOverflow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    showNewsBottomSheet(category.getNewsList().get(0));
                }
            });
        }

        private void showNewsBottomSheet(News news)
        {

            RcmdNewsBottomSheetDialogFragment rcmdNewsBottomSheetDialogFragment =
                    RcmdNewsBottomSheetDialogFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putParcelable(News.TAG, news);

            rcmdNewsBottomSheetDialogFragment.setArguments(bundle);

            rcmdNewsBottomSheetDialogFragment.show(mFragmentManager,
                    RcmdNewsBottomSheetDialogFragment.TAG);

        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        public RcmdNewsSubItemBinding mBinding;

        public ItemViewHolder(@NonNull RcmdNewsSubItemBinding binding)
        {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        public void bind(News news)
        {
            mBinding.setNews(news);
            mBinding.executePendingBindings();

        }
    }

    private void setLevelBar(String level, HeaderViewHolder holder)
    {
        View bar = holder.mBinding.levelBar;

        if(level.equals("1"))
        {
            bar.setBackgroundTintList(ContextCompat.
                    getColorStateList(mContext,R.color.sim_level_1));

        }
        else if(level.equals("2"))
        {
            bar.setBackgroundTintList(ContextCompat.
                    getColorStateList(mContext,R.color.sim_level_2));

        }
        else if(level.equals("3"))
        {
            bar.setBackgroundTintList(ContextCompat.
                    getColorStateList(mContext,R.color.sim_level_3));

        }
        else if(level.equals("4"))
        {
            bar.setBackgroundTintList(ContextCompat.
                    getColorStateList(mContext,R.color.sim_level_4));

        }
        else if(level.equals("5"))
        {
            bar.setBackgroundTintList(ContextCompat.
                    getColorStateList(mContext,R.color.sim_level_5));

        }

    }


}
