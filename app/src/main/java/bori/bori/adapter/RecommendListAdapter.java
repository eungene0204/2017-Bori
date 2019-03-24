package bori.bori.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import bori.bori.R;
import bori.bori.activity.WebViewActivity;
import bori.bori.news.News;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * Created by Eugene on 2017-03-07.
 */

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.ListItemViewHolder>
{
    public static final String TAG = "ListAdapter";
    private final Context mContext;
    private List<News> mData = Collections.emptyList();
    private int mFontSize;

    private OnNewsClickListener mOnNewsClickListener;

    public RecommendListAdapter(Context context,  List<News> data)
    {
        mContext = context;
        mData = data;
    }

    public void setNewsClickListener(OnNewsClickListener listener)
    {
        mOnNewsClickListener = listener;
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder
    {
        private Context mContext;

        private News mNews;

        public TextView mTitleView;
        public ImageView mNewsImgView;
        public TextView mCategoryView;
        public TextView mSourceView;
        public TextView mDateView;
        public ImageView mCardOverFlow;
        public ImageView mSourceLogo;

        public ListItemViewHolder(View v, Context context)
        {
            super(v);
            mContext = context;
            v.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    setFontSize(mOnNewsClickListener.onSetFontSize());
                    startWebViewActivity(getNews());
                }

            });

            mCategoryView = v.findViewById(R.id.card_category);
            mTitleView = (TextView) v.findViewById(R.id.card_title);
            mNewsImgView = (ImageView) v.findViewById(R.id.news_img);
            mSourceView = v.findViewById(R.id.card_source);
            mDateView = v.findViewById(R.id.card_date);
            mCardOverFlow = v.findViewById(R.id.card_overflow);
            mSourceLogo = v.findViewById(R.id.source_img);

            setRoundedImg();

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

            intent.putExtra(News.KEY_ID,news.getId());
            intent.putExtra(News.KEY_URL,news.getUrl());
            intent.putExtra(News.KEY_TITLE, news.getTitle());
            intent.putExtra(News.KEY_IMG_URL, news.getImgUrl());
            intent.putExtra(News.KEY_FONT_SIZE, getFontSize());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(intent);
        }

        //public TextView getTextView() { return mTextView; }
        //public ImageView getImageView() { return mImageView; }

        public News getNews()
        {
            return mNews;
        }

        public void setNews(News news)
        {
            mNews = news;
        }
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rcmd_news_card,parent,false);

        ListItemViewHolder listItemViewHolder = new ListItemViewHolder(v,mContext);

        return listItemViewHolder;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position)
    {
        News news = mData.get(position);

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

        holder.mCardOverFlow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showPopupMenu();
            }


        });

        Drawable logo = news.getSourceLogo();

        setSourceLogo(logo,holder);

        holder.setNews(news);

    }

    private void setSourceLogo(Drawable logo, ListItemViewHolder holder)
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

    private void showPopupMenu()
    {

    }


    private void setImageSrc(String url, int position, ImageView imageView, News news)
    {
        Log.i(TAG,mData.get(position).getTitle()) ;
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

    @Override
    public int getItemCount()
    {
        return mData.size();
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


}
