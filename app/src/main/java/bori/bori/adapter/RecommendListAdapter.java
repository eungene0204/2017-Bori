package bori.bori.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bori.bori.R;
import bori.bori.activity.WebViewActivity;
import bori.bori.fragment.RcmdNewsBottomSheetDialogFragment;
import bori.bori.news.News;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * Created by Eugene on 2017-03-07.
 */

public class RecommendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final String TAG = "RcmdAdapter";

    public final static int VIEW_TYPE_NEWS = 0;
    public final static int VIEW_TYPE_LOADING = 1;

    private final Context mContext;
    private final FragmentManager mFragmentManager;
    private List<News> mNewsList;
    private int mFontSize;

    private int mVisibleThreshold = 5;
    private int mLastVisibleItem, mTotalItemCount;
    private boolean mIsLoading;
    private OnLoadMoreListener mOnLoadMoreListener;


    private OnNewsClickListener mOnNewsClickListener;

    public RecommendListAdapter(Context context,  List<News> newsList, FragmentManager fragmentManager, RecyclerView recyclerView)

    {
        mContext = context;
        mNewsList = newsList;
        mFragmentManager = fragmentManager;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)
                recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                mTotalItemCount = linearLayoutManager.getItemCount();
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if(!mIsLoading && mTotalItemCount <= (mLastVisibleItem +mVisibleThreshold))
                {
                    if(mOnLoadMoreListener != null)
                    {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    mIsLoading = true;
                }
            }
        });
    }

    public void setNewsClickListener(OnNewsClickListener listener)
    {
        mOnNewsClickListener = listener;
    }

    public void add(int position,News news)
    {
        mNewsList.add(position, news);
        notifyItemInserted(position);
    }

    public void remove(News news)
    {
        int position = mNewsList.indexOf(news);
        mNewsList.remove(position);
        notifyItemRemoved(position);
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder
    {
        private Context mContext;

        private News mNews;

        public View mLevelBar;
        public TextView mTitleView;
        public ImageView mNewsImgView;
        public TextView mCategoryView;
        public TextView mSourceView;
        public TextView mDateView;
        public ImageView mCardOverFlow;
        public ImageView mSourceLogo;
        public CardView mCardView;

        public NewsViewHolder(View v, Context context)
        {
            super(v);
            mContext = context;

            mLevelBar = v.findViewById(R.id.level_bar);
            mCardView = v.findViewById(R.id.card_view);
            mCategoryView = v.findViewById(R.id.card_category);
            mTitleView = v.findViewById(R.id.card_title);
            mNewsImgView = v.findViewById(R.id.news_img);
            mSourceView = v.findViewById(R.id.card_source);
            mDateView = v.findViewById(R.id.card_date);
            mCardOverFlow = v.findViewById(R.id.card_overflow);
            mSourceLogo = v.findViewById(R.id.source_img);

            setRoundedImg();

            mCardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    setFontSize(mOnNewsClickListener.onSetFontSize());
                    startWebViewActivity(getNews());
                }
            });


            mCardOverFlow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showNewsBottomSheet(getNews());
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


        private void setRoundedImg()
        {
            GradientDrawable drawable = (GradientDrawable) mContext.getDrawable(R.drawable.round_background);
            mNewsImgView.setBackground(drawable);
            mNewsImgView.setClipToOutline(true);

        }


        private void startWebViewActivity(News news)
        {
            Intent intent = new Intent(mContext, WebViewActivity.class);

            intent.putExtra(News.TAG, news);
            intent.putExtra(News.KEY_URL_TYPE, WebViewActivity.TYPE_NEWS_URL);

            intent.putExtra(News.KEY_FONT_SIZE, getFontSize());


            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(intent);
        }


        public News getNews()
        {
            return mNews;
        }

        public void setNews(News news)
        {
            mNews = news;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder
    {
        ProgressBar mProgressBar;

        public LoadingViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.progressBar);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(viewType == VIEW_TYPE_NEWS)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rcmd_news_card,parent,false);

            return new NewsViewHolder(view,mContext);

        }
        else if(viewType == VIEW_TYPE_LOADING)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_loading,
                    parent,false);

            return new LoadingViewHolder(view);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {

        if(holder instanceof NewsViewHolder)
        {
            populateNewsRow((NewsViewHolder)holder,position);

        }
        else if(holder instanceof LoadingViewHolder)
        {
            showLoadingView((LoadingViewHolder)holder);

        }

    }

    private void showLoadingView(LoadingViewHolder holder)
    {
        holder.mProgressBar.setIndeterminate(true);
    }

    private void populateNewsRow(NewsViewHolder holder, int position)
    {
        News news = mNewsList.get(position);

        setLevelBar(news,holder);

        String category = news.getCategory();
        holder.mCategoryView.setText(category);

        String title = news.getTitle();
        holder.mTitleView.setText(title);

        ImageView imageView = holder.mNewsImgView;
        String imgUrl = news.getImgUrl();
        setImageSrc(imgUrl,position,imageView,news);

        String source = news.getSource();
        holder.mSourceView.setText(source);

        String date = news.getDate();
        holder.mDateView.setText(date);

        Drawable logo = news.getSourceLogo();

        setSourceLogo(logo,holder);

        holder.setNews(news);

    }

    private void setLevelBar(News news, NewsViewHolder holder)
    {
        View bar = holder.mLevelBar;
        String level = news.getSimilarityLevel();

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

    private void setSourceLogo(Drawable logo, NewsViewHolder holder)
    {
        if(null != logo)
        {
            holder.mSourceLogo.setImageDrawable(logo);
            holder.mSourceView.setVisibility(View.INVISIBLE);
            holder.mSourceLogo.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.mSourceLogo.setVisibility(View.INVISIBLE);
            holder.mSourceView.setVisibility(View.VISIBLE);
        }

    }


    private void setImageSrc(String url, int position, ImageView imageView, News news)
    {
        Log.i(TAG,mNewsList.get(position).getTitle()) ;
        Log.i(TAG,url);

        if(url.isEmpty())
        {
            imageView.setImageDrawable(news.getSourceLogo());
        }
        else
        {
            UrlImageViewHelper.setUrlDrawable(imageView,url, news.getSourceLogo(),6000);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener)
    {
        this.mOnLoadMoreListener = listener;
    }

    @Override
    public int getItemViewType(int position)
    {
        return mNewsList.get(position) == null ? VIEW_TYPE_LOADING :
                VIEW_TYPE_NEWS;
    }

    @Override
    public int getItemCount()
    {
        return mNewsList == null ? 0 : mNewsList.size();
    }

    public void setLoaded()
    {
        mIsLoading = false;

    }

    public int getFontSize()
    {
        return mFontSize;
    }

    public void setFontSize(int fontSize)
    {
        mFontSize = fontSize;
    }

    public interface OnNewsClickListener
    {
        int onSetFontSize();
    }

    public interface OnLoadMoreListener
    {
        void onLoadMore();
    }


}
