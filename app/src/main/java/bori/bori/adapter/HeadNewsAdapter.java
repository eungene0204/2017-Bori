package bori.bori.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import bori.bori.R;
import bori.bori.activity.WebViewActivity;
import bori.bori.fragment.bottom.HeadNewsBottomSheetFragment;
import bori.bori.news.Image.NewsImgManager;
import bori.bori.news.News;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.List;

public class HeadNewsAdapter extends RecyclerView.Adapter<HeadNewsAdapter.ListItemViewHolder>
{
    public static final String TAG = HeadNewsAdapter.class.getSimpleName();

    private Context mContext;
    private List<News> mData;
    private int mFontSize = 0;
    private final FragmentManager mFragmentManager;

    private OnHeadNewsClickListener mOnNewsClickListener;

    public HeadNewsAdapter(Context mContext, List<News> data, FragmentManager fragmentManager )
    {
        this.mContext = mContext;
        this.mData = data;
        this.mFragmentManager = fragmentManager;
    }

    public void setNewsClickListener(OnHeadNewsClickListener listener)
    {
        mOnNewsClickListener = listener;
    }


    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.head_news_item,parent, false);

        ListItemViewHolder listItemViewHolder = new ListItemViewHolder(v,mContext);

        return listItemViewHolder;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position)
    {
        News news = mData.get(position);

        setSourceLogo(news.getSourceLogo(), holder);
        String sourceText = news.getSource();
        holder.mSourceText.setText(sourceText);

        String title = news.getTitle();
        holder.mTitleText.setText(title);

        setNewsImg(holder, news, position);

        String date = news.getDate();
        holder.mDateText.setText(date);

        holder.setNews(news);
    }

    private void setNewsImg(ListItemViewHolder holder, News news, int position)
    {
        ImageView imageView = holder.mNewsImg;
        String imgUrl = news.getImgUrl();

        if(imgUrl.isEmpty())
        {
            imageView.setVisibility(View.GONE);
        }
        else
        {
            setImageSrc(imgUrl,position,imageView,news);
        }

    }

    private void setSourceLogo(Drawable logo, ListItemViewHolder holder)
    {
        if(null != logo)
        {
            holder.mSourceImg.setImageDrawable(logo);
            holder.mSourceText.setVisibility(View.INVISIBLE);
            holder.mSourceImg.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.mSourceImg.setVisibility(View.INVISIBLE);
            holder.mSourceText.setVisibility(View.VISIBLE);
        }

    }


    private void setImageSrc(String url, int position, ImageView imageView, News news)
    {
       Log.i(TAG,mData.get(position).getTitle()) ;
       Log.i(TAG,url);
       UrlImageViewHelper.setUrlDrawable(imageView,url, news.getSourceLogo(),6000);

    }


    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder
    {
        private Context mContext;

        private News mNews;

        public final TextView mSourceText;
        public final ImageView mSourceImg;
        public final TextView mTitleText;
        public final ImageView mNewsImg;
        public final TextView  mDateText;
        public final ImageView mOverflowImg;


        public ListItemViewHolder(View itemView,Context context)
        {
            super(itemView);
            mContext = context;

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    setFontSize(mOnNewsClickListener.onSetFontSize());
                    startWebViewActivity(getNews());

                }
            });

            mSourceText = itemView.findViewById(R.id.source_text);
            mSourceImg = itemView.findViewById(R.id.source_img);
            mTitleText= itemView.findViewById(R.id.title_text);
            mNewsImg = itemView.findViewById(R.id.news_img);
            mDateText = itemView.findViewById(R.id.date);
            mOverflowImg = itemView.findViewById(R.id.overflow_img);
            
            mOverflowImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showBottomSheet(getNews());
                    
                }
            });

            NewsImgManager.setRoundedImg(mNewsImg, mContext);

        }


        private void startWebViewActivity(News news)
        {
            Intent intent = new Intent(mContext, WebViewActivity.class);

            news.setCategory(mContext.getString(R.string.nav_head_news));

            intent.putExtra(News.TAG, news);
            intent.putExtra(News.KEY_URL_TYPE, WebViewActivity.TYPE_NEWS_URL);

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

    private void showBottomSheet(News news)
    {
         HeadNewsBottomSheetFragment headNewsBottomSheetFragment=
                    HeadNewsBottomSheetFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putParcelable(News.TAG,news );

            headNewsBottomSheetFragment.setArguments(bundle);


            headNewsBottomSheetFragment.show(mFragmentManager,
                    HeadNewsBottomSheetFragment.TAG);
    }

    public int getFontSize()
    {
        return mFontSize;
    }

    public void setFontSize(int fontSize)
    {
        mFontSize = fontSize;
    }


    public interface OnHeadNewsClickListener
    {
        int onSetFontSize();
    }


}
